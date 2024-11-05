package cn.treeh.Game.Combat;

import cn.treeh.Graphics.Rectangle;

public class Attack {
	public enum Type
			{
				CLOSE,
				RANGED,
				MAGIC
			};

	public enum DamageType
			{
				DMG_WEAPON,
				DMG_MAGIC,
				DMG_FIXED
			};

	public Type type = Type.CLOSE;
	DamageType damagetype = DamageType.DMG_WEAPON;

	public double mindamage = 1.0;
	public double maxdamage = 1.0;
	public float critical = 0.0f;
	public float ignoredef = 0.0f;
	int matk = 0;
	public int accuracy = 0;
	int fixdamage = 0;
	public int playerlevel = 1;

	int hitcount = 0;
	int mobcount = 0;
	public int speed = 0;
	int stance = 0;
	int skill = 0;
	public int bullet = 0;

	public int[] origin = new int[2];
	public Rectangle range;
	float hrange = 1.0f;
	public boolean toleft = false;

}