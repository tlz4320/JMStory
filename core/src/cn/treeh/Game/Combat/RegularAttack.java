package cn.treeh.Game.Combat;

import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Job.Job;
import cn.treeh.Graphics.Animation;

public class RegularAttack extends SpecialMove{
    RegularAction action;
	RegularBullet bullet;
    public void  apply_useeffects(Char c) {

    }

	void apply_actions(Char user, Attack.Type type)
	{
		action.apply(user, type);
	}

	public void apply_stats( Char user, Attack attack)
	{
		attack.damagetype = Attack.DamageType.DMG_WEAPON;
		attack.skill = 0;
		attack.mobcount = 1;
		attack.hitcount = 1;
		attack.stance = user.get_look().getStrance().ordinal();

		if (attack.type == Attack.Type.CLOSE)
			attack.range = user.get_afterimage().getRange();
	}

	public void apply_hiteffects( AttackUser au, Mob m)  {}

	public Animation get_bullet(Char user, int bulletid)
	{
		return bullet.get(user, bulletid);
	}

	public boolean is_attack()
	{
		return true;
	}

	public boolean is_skill()
	{
		return false;
	}

	public int get_id()
	{
		return 0;
	}

	public SpecialMove.ForbidReason can_use(int i, Weapon.Type weapon, Job j, int i2, int i3, int bullets)
	{
		switch (weapon)
		{
		case BOW:
		case CROSSBOW:
		case CLAW:
		case GUN:
			return bullets != 0 ? SpecialMove.ForbidReason.FBR_NONE : SpecialMove.ForbidReason.FBR_BULLETCOST;
		default:
			return SpecialMove.ForbidReason.FBR_NONE;
		}
	}
}