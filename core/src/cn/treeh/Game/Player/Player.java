package cn.treeh.Game.Player;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Inventory.Inventory;
import cn.treeh.Game.MapleMap.Ladder;
import cn.treeh.Game.Movement;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Player.Buffer.Buff;
import cn.treeh.Game.Player.Buffer.BuffStat;
import cn.treeh.Game.Player.Buffer.PassiveBuffs;
import cn.treeh.Game.Player.Look.CharLook;
import cn.treeh.Util.TimedBool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class Player extends Char{
    CharStat stats;
    Inventory inventory;
//    SkillBook skillbook;
//    QuestLog questlog;
//    TeleportRock teleportrock;
    MonsterBook monsterbook;

    HashMap<BuffStat.Id, Buff> buffs = new HashMap<>();
    ActiveBuffs active_buffs;
    PassiveBuffs passive_buffs;

    TreeMap<Integer, Integer> cooldowns = new TreeMap<>();

    HashMap<KeyAction.Id, Boolean> keysdown = new HashMap<>();

    Movement lastmove;

    Random randomizer = new Random();
//
    Ladder ladder;
    TimedBool climb_cooldown = new TimedBool();

    boolean underwater;
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
        physics.move(phobj);
        return getLayer();
    }
    public void respawn(int[] pos, boolean uw)
    {
        setPos(pos[0], pos[1]);
        underwater = uw;
        attacking = false;
        ladder = null;
//        nullstate.update_state(*this);
    }
    public float getWalkSpeed()
    {
        return 0.05f + 0.11f * stats.get_total(EquipStat.Id.SPEED) / 100;
    }
}
