package cn.treeh.Game.Player;

import cn.treeh.Game.Movement;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Player.Look.CharLook;
import cn.treeh.IO.Net.LookEntry;

import java.util.LinkedList;
import java.util.TreeMap;

public class OtherChar extends Char {
    int level;
		int job;
        LinkedList<Movement> movements = new LinkedList<>();
		Movement lastmove;
		int timer;

		TreeMap<Integer, Integer> skilllevels;
		int attackspeed;

        OtherChar(int charid, CharLook look, int level, int job,
                  String name, int stance, int[] position)
	{
		super(charid, look, name);
		this.level = level;
		this.job =job;
		setPos(position[0], position[1]);

		lastmove.xpos = position[0];
		lastmove.ypos = position[1];
		lastmove.newstate = stance;
		timer = 0;

		attackspeed = 6;
		attacking = false;
	}
	public int update(Physics physics)
	{
		if (timer > 1)
		{
			timer--;
		}
		else if (timer == 1)
		{
			if (!movements.isEmpty())
			{
				lastmove = movements.peek();
				movements.pop();
			}
			else
			{
				timer = 0;
			}
		}

		if (!attacking)
		{
			int laststate = lastmove.newstate;
			setState(laststate);
		}

		phobj.hspeed = lastmove.xpos - phobj.crnt_x();
		phobj.vspeed = lastmove.ypos - phobj.crnt_y();
		phobj.move();

		physics.getFht().update_fh(phobj);

		boolean aniend = super.update(physics, getStancespeed());

		if (aniend && attacking)
			attacking = false;

		return getLayer();
	}

	void send_movement(LinkedList<Movement> newmoves)
	{
		movements.push(newmoves.peekLast());

		if (timer == 0)
		{
			timer = 50;
		}
	}

	void update_skill(int skillid, int skilllevel)
	{
		skilllevels.put(skillid, skilllevel);
	}

	void update_speed(int as)
	{
		attackspeed = as;
	}

	void update_look( LookEntry newlook)
	{
		look = new CharLook(newlook);

		int laststate = lastmove.newstate;
		setState(laststate);
	}

	int get_integer_attackspeed()
	{
		return attackspeed;
	}

	int get_level()
	{
		return level;
	}

	int get_skilllevel(int skillid)
	{
		return skilllevels.getOrDefault(skillid, 0);
	}
}