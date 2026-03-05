package cn.treeh.Game.Combat;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class AttackResult {
    public AttackResult() {}

	public	AttackResult(Attack attack)
		{
			type = attack.type;
			hitcount = attack.hitcount;
			skill = attack.skill;
			speed = attack.speed;
			stance = attack.stance;
			bullet = attack.bullet;
			toleft = attack.toleft;
		}

		Attack.Type type;
		int attacker = 0;
		public int mobcount = 0;
		int hitcount = 1;
		int skill = 0;
		int charge = 0;
		int bullet = 0;
		int level = 0;
		int display = 0;
		int stance = 0;
		int speed = 0;
		boolean toleft = false;
		public TreeMap<Integer, LinkedList<Map.Entry<Integer, Boolean>>> damagelines = new TreeMap<>();
		public int first_oid;
		public int last_oid;
}