package cn.treeh.Game.Combat;

import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.Player.Char;

public class RegularAction extends SkillAction {
    public void apply(Char target, Attack.Type atype)
	{
		Weapon.Type weapontype = target.get_weapontype();
		boolean degenerate;

		switch (weapontype)
		{
		case BOW:
		case CROSSBOW:
		case CLAW:
		case GUN:
			degenerate = atype != Attack.Type.RANGED;
			break;
		default:
			degenerate = false;
			break;
		}

		target.attack(degenerate);
	}
}