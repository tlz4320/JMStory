package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Combat.*;
import cn.treeh.Game.Movement;
import cn.treeh.Game.Physics.MovingObject;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Spawn;
import cn.treeh.Graphics.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MapMobs {
	MapObjects mobs;

	LinkedList<Spawn.MobSpawn> spawns = new LinkedList<>();

	public void  draw(int layer, double viewx, double viewy, float alpha, SpriteBatch batch)
	{
		mobs.draw(layer, viewx, viewy, alpha, batch);
	}

	public void  update( Physics physics)
	{
		for (Spawn.MobSpawn spawn : spawns)
		{
			MapObject mob = mobs.get(spawn.get_oid());
			if (mob instanceof Mob)
			{
				int mode = spawn.get_mode();

				if (mode > 0)
					((Mob)mob).set_control(mode);

				mob.makeActive();
			}
			else
			{
				mobs.add(spawn.instantiate());
			}
		}

		mobs.update(physics);
	}

	public void  spawn(Spawn.MobSpawn spawn)
	{
		spawns.add(spawn);
	}

	public void  remove(int oid, int animation)
	{
		MapObject mob = mobs.get(oid);
		if (mob instanceof  Mob)
			((Mob)mob).kill(animation);
	}

	public void  clear()
	{
		mobs.clear();
	}

	public void  set_control(int oid, boolean control)
	{
		int mode = control ? 1 : 0;
		MapObject mob = mobs.get(oid);
		if (mob instanceof Mob)
			((Mob)mob).set_control(mode);
	}

	public void  send_mobhp(int oid, int percent, int playerlevel)
	{MapObject mob = mobs.get(oid);
		if (mob instanceof Mob)
			((Mob)mob).show_hp(percent, playerlevel);
	}

	public void  send_movement(int oid, int[] start, LinkedList<Movement> movements)
	{
		MapObject mob = mobs.get(oid);
		if (mob instanceof Mob)
			((Mob)mob).send_movement(start,movements);
	}

	public void  send_attack(AttackResult result, Attack attack,
							 List<Integer> targets, int mobcount)
	{
		for (int target : targets)
		{
			MapObject mob = mobs.get(target);
			if (mob instanceof Mob)
			{
				result.damagelines.put(target, ((Mob)mob).calculate_damage(attack));
				result.mobcount++;

				if (result.mobcount == 1)
					result.first_oid = target;

				if (result.mobcount == mobcount)
					result.last_oid = target;
			}
		}
	}

	public void  apply_damage(int oid, int damage, boolean toleft,
							  AttackUser user, SpecialMove move)
	{
		MapObject mob = mobs.get(oid);
		if (mob instanceof Mob)
		{
			((Mob)mob).apply_damage(damage, toleft);

			// TODO: Maybe move this into the method above too?
			move.apply_hiteffects(user, ((Mob)mob));
		}
	}

	public boolean  contains(int oid)
	{
		return mobs.contains(oid);
	}

	public int  find_colliding( MovingObject moveobj)
	{
		int[] horizontal = new int[]{moveobj.get_last_x(), moveobj.get_x()};
		int[] vertical = new int[]{moveobj.get_last_y(), moveobj.get_y()};

		Rectangle player_rect = new Rectangle(Math.min(horizontal[0], horizontal[1]),
											  Math.max(horizontal[0], horizontal[1]),
											  Math.min(vertical[0], vertical[1]) - 50,
											  Math.max(vertical[0], vertical[1]));
		for(Map.Entry<Integer, MapObject> obj : mobs.objects.entrySet()){
			if(obj.getValue() instanceof Mob){
				Mob mob = (Mob)obj;
				if(mob.is_alive() && mob.is_in_range(player_rect)){
					return mob.getOid();
				}
			}
		}
		return 0;
	}

	public MobAttack create_attack(int oid)
	{
		MapObject mob = mobs.get(oid);
		if (mob instanceof Mob)
			return ((Mob)mob).create_touch_attack();
		else
			return new MobAttack();
	}

	public int[]  get_mob_position(int oid)
	{
		MapObject mob = mobs.get(oid);
		if (mob instanceof Mob)
			return mob.getPosition();
		else
			return new int[]{0, 0};
	}

	public  int[] get_mob_head_position(int oid)
	{
		MapObject mob = mobs.get(oid);
		if (mob instanceof Mob)
			return ((Mob) mob).get_head_position();
		else
			return new int[]{0, 0};
	}

	public MapObjects  get_mobs()
	{
		return mobs;
	}
}
