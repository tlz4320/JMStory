package cn.treeh.Game.Player;

import cn.treeh.Game.Combat.Attack;
import cn.treeh.Game.Combat.MobAttack;
import cn.treeh.Game.Combat.MobAttackResult;
import cn.treeh.Game.Combat.SpecialMove;
import cn.treeh.Game.Data.WeaponData;
import cn.treeh.Game.GamePlay;
import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Inventory.Inventory;
import cn.treeh.Game.Inventory.InventoryType;
import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.MapleMap.Ladder;
import cn.treeh.Game.MapleMap.MapInfo;
import cn.treeh.Game.Movement;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Player.Buffer.ActiveBuffs;
import cn.treeh.Game.Player.Buffer.Buff;
import cn.treeh.Game.Player.Buffer.BuffStat;
import cn.treeh.Game.Player.Buffer.PassiveBuffs;
import cn.treeh.Game.Player.Job.Job;
import cn.treeh.Game.Player.Job.SkillBook;
import cn.treeh.Game.Player.Look.CharLook;
import cn.treeh.Game.Player.Look.EquipSlot;
import cn.treeh.Game.Player.PlayerStats.*;
import cn.treeh.IO.Net.CharEntry;
import cn.treeh.UI.Elements.UIElement;
import cn.treeh.UI.Elements.UIStatsInfo;
import cn.treeh.UI.UI;
import cn.treeh.Util.TimedBool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

public class Player extends Char{
    CharStat stats;
    Inventory inventory;
    SkillBook skillbook;
    QuestLog questlog;
    TeleportRock teleportrock;
    MonsterBook monsterbook;

    HashMap<BuffStat.Id, Buff> buffs = new HashMap<>();
    ActiveBuffs active_buffs;
    PassiveBuffs passive_buffs;

    TreeMap<Integer, Integer> cooldowns = new TreeMap<>();
    Movement lastmove;

    Random randomizer = new Random();
    //
    Ladder ladder;
    TimedBool climb_cooldown = new TimedBool();

    boolean underwater;

    public Player(CharEntry entry)
    {

        super(entry.id, new CharLook(entry.look), entry.stats.name);
        GamePlay.stage.addListener(new InputListener() {
            @Override
            public boolean keyUp(InputEvent event, int keycode) {
                sendAction(false);
                return true;
            }
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                sendAction(true);
                return true;
            }
        });
        stats = new CharStat(entry.stats);
        attacking = false;
        underwater = false;
        set_state(Char.State.STAND);
        set_direction(true);
    }

    public Player(int o, CharLook lk, String name) {
        super(o, lk, name);
    }
    public Player(){
        super(0, new CharLook(), "");
    }
    public Ladder getLadder(){
        return ladder;
    }

    public CharStat getStats() {
        return stats;
    }

    public void setState(Char.State st){
        if(!attacking){
            super.setState(st);

        }
    }
    public void draw(int layer, double[] realpos, float alpha, SpriteBatch batch){
        if(layer == getLayer())
            super.draw(realpos[0], realpos[1], alpha, batch);
    }
    public int update(Physics physics){
        PlayerStat pst = get_state(state);

        if (pst != null)
        {
            pst.update(this);
            physics.move(phobj);

            boolean aniend = super.update(physics, getStancespeed());

            if (aniend && attacking)
            {
                attacking = false;
                nullstate.update_state(this);
            }
            else
            {
                pst.update_state(this);
            }
        }

        int stancebyte = facing_right ? state.ordinal() : state.ordinal() + 1;
        Movement newmove = new Movement(phobj, stancebyte);
        boolean needupdate = lastmove.hasmoved(newmove);

        if (needupdate)
        {
            MovePlayerPacket(newmove).dispatch();
            lastmove = newmove;
        }

        climb_cooldown.update();

        physics.move(phobj);
        return getLayer();
    }
    public void respawn(int[] pos, boolean uw)
    {
        setPos(pos[0], pos[1]);
        underwater = uw;
        attacking = false;
        ladder = null;
        nullstate.update_state(this);
    }
    public float getWalkSpeed()
    {
        return 0.05f + 0.11f * stats.get_total(EquipStat.Id.SPEED) / 100;
    }
    static PlayerNullState nullstate = new PlayerNullState();
    static PlayerStandState standing = new PlayerStandState();
    static PlayerWalkState walking = new PlayerWalkState();
    static PlayerFallState falling = new PlayerFallState();
    static PlayerProneState lying = new PlayerProneState();
    static PlayerClimbState climbing = new PlayerClimbState();
    static PlayerSitState sitting = new PlayerSitState();
    static PlayerFlyState flying = new PlayerFlyState();
    PlayerStat get_state(Char.State state)
    {


        switch (state)
        {
            case STAND:
                return standing;
                case WALK:
                    return walking;
                    case FALL:
                        return falling;
                        case PRONE:
                            return lying;
                            case LADDER:
                                case ROPE:
                                    return climbing;
                                    case SIT:
                                        return sitting;
                                        case SWIM:
                                            return flying;
                                            default:
                                                return null;
        }
    }
    void sendAction(boolean down){
        PlayerStat pst = get_state(state);
        if(pst != null)
            pst.sendAction(this, down);
    }
    public void recalcStats(boolean equipchanged){
        Weapon.Type weapontype = get_weapontype();

        stats.set_weapontype(weapontype);
        stats.init_totalstats();

        if (equipchanged)
            inventory.recalc_stats(weapontype);

        for (EquipStat.Id stat : EquipStat.Id.values())
        {
            int inventory_total = inventory.get_stat(stat);
            stats.add_value(stat, inventory_total);
        }

        Map<Integer, Integer> passive_skills = skillbook.collect_passives();

        for (Map.Entry<Integer, Integer> passive : passive_skills.entrySet())
        {
            int skill_id = passive.getKey();
            int skill_level = passive.getValue();

            passive_buffs.apply_buff(stats, skill_id, skill_level);
        }

        for (Buff buff : buffs.values())
            active_buffs.apply_buff(stats, buff.stat, buff.value);

        stats.close_totalstats();
        if(UI.getInstance().getState().get(UIElement.Type.STATSINFO) != null){
            ((UIStatsInfo)UI.getInstance().getState().get(UIElement.Type.STATSINFO)).update_all_stats();
        }
    }
    public void change_equip(int slot)
    {
        int itemid = inventory.get_item_id(InventoryType.Id.EQUIPPED, slot);
        if (itemid != 0)
            look.add_equip(itemid);
        else
            look.remove_equip(EquipSlot.Id.values()[slot]);
    }
    public void use_item(int itemid)
    {
        InventoryType.Id type = InventoryType.by_item_id(itemid);
        int slot = inventory.find_item(type, itemid);
        if (slot != 0)
            if (type == InventoryType.Id.USE)
                UseItemPacket(slot, itemid).dispatch();
    }
    public int get_integer_attackspeed()
    {
        int weapon_id = look.getEquips().getWeapon();

        if (weapon_id <= 0)
            return 0;

        WeaponData weapon = WeaponData.get(weapon_id);

        int base_speed = stats.get_attackspeed();
        int weapon_speed = weapon.get_speed();
        return base_speed + weapon_speed;
    }
    public void set_direction(boolean flipped)
    {
        if (!attacking)
            super.set_direction(flipped);
    }
    public void set_state(State st)
    {
        if (!attacking)
        {
            super.set_state(st);

            PlayerStat pst = get_state(st);

            if (pst != null)
                pst.initialize(this);
        }
    }
    boolean is_attacking()
    {
        return attacking;
    }

    boolean can_attack()
    {
        return !attacking && !is_climbing() && !is_sitting() && look.getEquips().hasWeapon();
    }


    public Attack prepare_attack(boolean skill)
    {
        Attack.Type attacktype;
        boolean degenerate;

        if (state == Char.State.PRONE)
        {
            degenerate = true;
            attacktype = Attack.Type.CLOSE;
        }
        else
        {
            Weapon.Type weapontype;
            weapontype = get_weapontype();

            switch (weapontype)
            {
                case BOW:
                    case CROSSBOW:
                        case CLAW:
                            case GUN:
                            {
                                degenerate = !inventory.has_projectile();
                                attacktype = degenerate ? Attack.Type.CLOSE : Attack.Type.RANGED;
                                break;
                            }
                case WAND:
                    case STAFF:
                    {
                        degenerate = !skill;
                        attacktype = degenerate ? Attack.Type.CLOSE : Attack.Type.MAGIC;
                        break;
                    }
                default:
                {
                    attacktype = Attack.Type.CLOSE;
                    degenerate = false;
                    break;
                }
            }
        }

        Attack attack = new Attack();
        attack.type = attacktype;
        attack.mindamage = stats.get_mindamage();
        attack.maxdamage = stats.get_maxdamage();

        if (degenerate)
        {
            attack.mindamage /= 10;
            attack.maxdamage /= 10;
        }

        attack.critical = stats.get_critical();
        attack.ignoredef = stats.get_ignoredef();
        attack.accuracy = stats.get_total(EquipStat.Id.ACC);
        attack.playerlevel = stats.get_stat(MapleStat.Id.LEVEL);
        attack.range = stats.get_range();
        attack.bullet = inventory.get_bulletid();
        attack.origin = getPosition();
        attack.toleft = !facing_right;
        attack.speed = get_integer_attackspeed();

        return attack;
    }

    public void rush(double targetx)
    {
        if (phobj.onground)
        {
            int delay = getAttackdelay(1);
            phobj.movexuntil(targetx, delay);
            phobj.set_flag(PhysicsObject.Flag.TURNATEDGES);
        }
    }

    public boolean is_invincible()
    {
        if (state == Char.State.DIED)
            return true;

        if (has_buff(BuffStat.Id.DARKSIGHT))
            return true;

        return super.is_invincible();
    }

    public MobAttackResult damage(MobAttack attack)
    {
        int damage = stats.calculate_damage(attack.watk);
        showDamage(damage);

        boolean fromleft = attack.origin[0] > phobj.get_x();

        boolean missed = damage <= 0;
        boolean immovable = ladder != null || state == Char.State.DIED;
        boolean knockback = !missed && !immovable;

        if (knockback && randomizer.nextDouble(1.0) > stats.get_stance())
        {
            phobj.hspeed = fromleft ? -1.5 : 1.5;
            phobj.vforce -= 3.5;
        }

        int direction = fromleft ? 0 : 1;

        return new MobAttackResult(attack, damage, direction);
    }

    public void give_buff(Buff buff)
    {
        buffs.put(buff.stat, buff);
    }

    public void cancel_buff(BuffStat.Id stat)
    {
        buffs.remove(stat);
    }

    public boolean has_buff(BuffStat.Id stat)
    {
        return buffs.containsKey(stat);
    }

    public void change_skill(int skill_id, int skill_level, int masterlevel, long expiration)
    {
        int old_level = skillbook.get_level(skill_id);
        skillbook.set_skill(skill_id, skill_level, masterlevel, expiration);

        if (old_level != skill_level)
            recalcStats(false);
    }

    public void add_cooldown(int skill_id, int cooltime)
    {
        cooldowns.put(skill_id, cooltime);
    }

    public boolean has_cooldown(int skill_id)
    {
        int iter = cooldowns.getOrDefault(skill_id, 0);

        return iter > 0;
    }
    public SpecialMove.ForbidReason can_use(SpecialMove move)
    {
        if (move.is_skill() && state == Char.State.PRONE)
			return SpecialMove.ForbidReason.FBR_OTHER;

		if (move.is_attack() && (state == Char.State.LADDER || state == Char.State.ROPE))
			return SpecialMove.ForbidReason.FBR_OTHER;

		if (has_cooldown(move.get_id()))
			return SpecialMove.ForbidReason.FBR_COOLDOWN;

		int level = skillbook.get_level(move.get_id());
		Weapon.Type weapon = get_weapontype();
		Job job = stats.get_job();
		int hp = stats.get_stat(MapleStat.Id.HP);
		int mp = stats.get_stat(MapleStat.Id.MP);
		int bullets = inventory.get_bulletcount();

		return move.can_use(level, weapon, job, hp, mp, bullets);
	}
    public void change_level(int level)
	{
		int oldlevel = get_level();

		if (level > oldlevel)
			showEffectId(CharEffect.Id.LEVELUP);

		stats.set_stat(MapleStat.Id.LEVEL, level);
	}

    public int get_level()
	{
		return stats.get_stat(MapleStat.Id.LEVEL);
	}

    public int get_skilllevel(int skillid)
	{
		return skillbook.get_level(skillid);
	}

    public void change_job(int jobid)
	{
		showEffectId(CharEffect.Id.JOBCHANGE);
		stats.change_job(jobid);
	}

    public void set_seat(MapInfo.Seat seat)
	{
		if (seat != null)
		{
            int[] pos = seat.getpos();
			setPos(pos[0], pos[1]);
			set_state(Char.State.SIT);
		}
	}

    public void set_ladder(Ladder ldr)
	{
		ladder = ldr;

		if (ladder != null)
		{
			phobj.set_x(ldr.get_x());

			phobj.hspeed = 0.0;
			phobj.vspeed = 0.0;
			phobj.fhlayer = 7;

			set_state(ldr.is_ladder() ? Char.State.LADDER : Char.State.ROPE);
		}
	}

    public void set_climb_cooldown()
	{
		climb_cooldown.setTime(1000);
	}

    public boolean can_climb()
	{
		return !climb_cooldown.get();
	}

    public float get_walkforce()
	{
		return 0.05f + 0.11f * stats.get_total(EquipStat.Id.SPEED) / 100f;
	}

    public float get_jumpforce()
	{
		return 1.0f + 3.5f * stats.get_total(EquipStat.Id.JUMP) / 100f;
	}

    public float get_climbforce()
	{
		return stats.get_total(EquipStat.Id.SPEED) / 100f;
	}

    public float get_flyforce()
	{
		return 0.25f;
	}

    public boolean is_underwater()
	{
		return underwater;
	}

    public CharStat get_stats()
	{
		return stats;
	}

    public  Inventory get_inventory()
	{
		return inventory;
	}

    public SkillBook get_skills()
	{
		return skillbook;
	}

    public QuestLog get_quests()
	{
		return questlog;
	}

    public  TeleportRock get_teleportrock()
	{
		return teleportrock;
	}

    public  MonsterBook get_monsterbook()
	{
		return monsterbook;
	}

    public Ladder get_ladder()
	{
		return ladder;
	}
}

