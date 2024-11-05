package cn.treeh.Game.Combat;

public class MobAttackResult {
	int damage;
	int mobid;
	int oid;
	int direction;

	public MobAttackResult(MobAttack attack, int damage, int direction)
	{
		this.damage = damage;
		this.direction = direction;
		this.mobid = attack.mobid;
		this.oid =attack.oid;
	}
}