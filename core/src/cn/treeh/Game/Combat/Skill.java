package cn.treeh.Game.Combat;

import cn.treeh.Game.Data.SkillData;
import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.MapleMap.Mob;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Job.Job;
import cn.treeh.Game.Player.Job.SkillId;
import cn.treeh.Graphics.Animation;
import cn.treeh.NX.Bitmap;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.StringUtil;

public class Skill {
    SkillAction action;
		SkillBullet bullet;
		SkillSound sound;
		SkillUseEffect useeffect;
		SkillHitEffect hiteffect;

		int skillid;
		boolean overregular;
		boolean projectile;
        public Skill(int id)
	{
		skillid = id;
		 SkillData data = SkillData.getSkillData(skillid);

		String strid = "" + skillid;

		if (skillid < 10000000)
			strid = StringUtil.extend(skillid, 7);
		Node src = NXFiles.Skill().subNode(strid.substring(0, 3) + ".img").subNode("skill").subNode(strid);

		projectile = true;
		overregular = false;

		sound = new SingleSkillSound(strid);

		boolean byleveleffect = src.subNode("CharLevel").
		subNode("10").subNode("effect").nChild() > 0;
		boolean multieffect = src.subNode("effect0").nChild() > 0;

		if (byleveleffect)
		{
			useeffect = new ByLevelUseEffect(src);
		}
		else if (multieffect)
		{
			useeffect = new MultiUseEffect(src);
		}
		else
		{
			boolean isanimation = src.subNode("effect").subNode("0").getType() == Node.Type.bitmap;
			boolean haseffect1 = src.subNode("effect").subNode("1").nChild() > 0;

			if (isanimation)
			{
				useeffect = new SingleUseEffect(src);
			}
			else if (haseffect1)
			{
				useeffect = new TwoHandedUseEffect(src);
			}
			else
			{
				SkillId.Id tmpid = SkillId.idmap.getOrDefault(skillid, SkillId.Id.NONE_ID);
				switch (tmpid)
				{

				case IRON_BODY:
				case MAGIC_ARMOR:
					useeffect = new IronBodyUseEffect();
					break;
				default:
					useeffect = new NoUseEffect();
					break;
				}
			}
		}

		boolean bylevelhit = src.subNode("CharLevel").subNode("10").subNode("hit").nChild() > 0;
		boolean byskilllevelhit = src.subNode("level").subNode("1").subNode("hit").nChild() > 0;
		boolean hashit0 = src.subNode("hit").subNode("0").nChild() > 0;
		boolean hashit1 = src.subNode("hit").subNode("1").nChild() > 0;

		if (bylevelhit)
		{
			if (hashit0 && hashit1)
				hiteffect = new ByLevelTwoHHitEffect(src);
			else
				hiteffect = new ByLevelHitEffect(src);
		}
		else if (byskilllevelhit)
		{
			hiteffect = new BySkillLevelHitEffect(src);
		}
		else if (hashit0 && hashit1)
		{
			hiteffect = new TwoHandedHitEffect(src);
		}
		else if (hashit0)
		{
			hiteffect = new SingleHitEffect(src);
		}
		else
		{
			hiteffect = new NoHitEffect();
		}

		boolean hasaction0 = src.subNode("action").subNode("0").getType() == Node.Type.string;
		boolean hasaction1 = src.subNode("action").subNode("1").getType() == Node.Type.string;

		if (hasaction0 && hasaction1)
		{
			action = new cn.treeh.Game.Combat.TwoHandedAction(src);
		}
		else if (hasaction0)
		{
			action = new SingleAction(src);
		}
		else if (data.is_attack())
		{
			boolean bylevel = src.subNode("level").subNode("1").subNode("action").getType() == Node.Type.string;

			if (bylevel)
			{
				action = new ByLevelAction(src, skillid);
			}
			else
			{
				action = new RegularAction();
				overregular = true;
			}
		}
		else
		{
			action = new NoAction();
		}

		boolean hasball = src.subNode("ball").nChild() > 0;
		boolean bylevelball = src.subNode("level").subNode("1").subNode("ball").nChild() > 0;

		if (bylevelball)
		{
			bullet = new BySkillLevelBullet(src, skillid);
		}
		else if (hasball)
		{
			bullet = new SingleBullet(src);
		}
		else
		{
			bullet = new RegularBullet();
			projectile = false;
		}
	}

	public void apply_useeffects(Char user)
	{
		useeffect.apply(user);

		sound.play_use();
	}

	public void apply_actions(Char user, Attack. Type type)
	{
		action.apply(user, type);
	}

	public void apply_stats( Char user, Attack attack)
	{
		attack.skill = skillid;

		int level = user.get_skilllevel(skillid);
		 SkillData. Stats stats = SkillData.getSkillData(skillid).get_stats(level);

		if (stats.fixdamage != 0)
		{
			attack.fixdamage = stats.fixdamage;
			attack.damagetype = Attack.DamageType.DMG_FIXED;
		}
		else if (stats.matk != 0)
		{
			attack.matk += stats.matk;
			attack.damagetype = Attack.DamageType.DMG_MAGIC;
		}
		else
		{
			attack.mindamage *= stats.damage;
			attack.maxdamage *= stats.damage;
			attack.damagetype = Attack.DamageType.DMG_WEAPON;
		}

		attack.critical += stats.critical;
		attack.ignoredef += stats.ignoredef;
		attack.mobcount = stats.mobcount;
		attack.hrange = stats.hrange;

		switch (attack.type)
		{
		case RANGED:
			attack.hitcount = stats.bulletcount;
			break;
		default:
			attack.hitcount = stats.attackcount;
			break;
		}

		if (!stats.range.empty())
			attack.range = stats.range;

		if (projectile && !(attack.bullet == 0))
		{
			SkillId.Id tmpid = SkillId.idmap.getOrDefault(skillid, SkillId.Id.NONE_ID);
			switch (tmpid)
			{
				case THREE_SNAILS:
				switch (level)
				{
				case 1:
					attack.bullet = 4000019;
					break;
				case 2:
					attack.bullet = 4000000;
					break;
				case 3:
					attack.bullet = 4000016;
					break;
				}
				break;
			default:
				attack.bullet = skillid;
				break;
			}
		}

		if (overregular)
		{
			attack.stance = user.get_look().getStrance().ordinal();

			if (attack.type == Attack.Type.CLOSE && !projectile)
				attack.range = user.get_afterimage().getRange();
		}
	}

	public void apply_hiteffects( AttackUser user, Mob target)
	{
		hiteffect.apply(user, target);

		sound.play_hit();
	}

	public Animation get_bullet(Char user, int bulletid)
	{
		return bullet.get(user, bulletid);
	}

	public boolean is_attack()
	{
		return SkillData.getSkillData(skillid).is_attack();
	}

	public boolean is_skill()
	{
		return true;
	}

	public int get_id()
	{
		return skillid;
	}

	public SpecialMove.ForbidReason can_use(int level, Weapon.Type weapon, Job job, int hp, int mp, int bullets)
	{
		SkillData data = SkillData.getSkillData(skillid);
		if (level <= 0 || level > data.get_masterlevel())
			return SpecialMove.ForbidReason.FBR_OTHER;

		if (!job.can_use(skillid))
			return SpecialMove.ForbidReason.FBR_OTHER;

		 SkillData.Stats stats = data.get_stats(level);

		if (hp <= stats.hpcost)
			return SpecialMove.ForbidReason.FBR_HPCOST;

		if (mp < stats.mpcost)
			return SpecialMove.ForbidReason.FBR_MPCOST;

		Weapon. Type reqweapon = data.get_required_weapon();

		if (weapon != reqweapon && reqweapon != Weapon.Type.NONE)
			return SpecialMove.ForbidReason.FBR_WEAPONTYPE;

		switch (weapon)
		{
		case BOW:
		case CROSSBOW:
		case CLAW:
		case GUN:
			return (bullets >= stats.bulletcost) ? SpecialMove.ForbidReason.FBR_NONE :
			SpecialMove.ForbidReason.FBR_BULLETCOST;
		default:
			return SpecialMove.ForbidReason.FBR_NONE;
		}
	}


}