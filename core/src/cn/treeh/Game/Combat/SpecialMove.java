package cn.treeh.Game.Combat;

import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Job.Job;

public abstract class SpecialMove {
	abstract public ForbidReason can_use(int level, Weapon.Type weapon, Job job, int hp, int mp, int bullets);

	public enum ForbidReason
			{
				FBR_NONE,
				FBR_WEAPONTYPE,
				FBR_HPCOST,
				FBR_MPCOST,
				FBR_BULLETCOST,
				FBR_COOLDOWN,
				FBR_OTHER
			};
	abstract public boolean is_attack();
	abstract public boolean is_skill();
	abstract public int get_id();

}