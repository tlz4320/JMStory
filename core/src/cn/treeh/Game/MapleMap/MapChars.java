package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Movement;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Player.OtherChar;
import cn.treeh.Game.Spawn;
import cn.treeh.IO.Net.LookEntry;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.LinkedList;
import java.util.List;

public class MapChars {
	MapObjects chars;

	LinkedList<Spawn.CharSpawn> spawns = new LinkedList<>();
	public void draw(int layer, double viewx, double viewy, float alpha, SpriteBatch batch)
	{
		chars.draw(layer, viewx, viewy, alpha, batch);
	}

	public void update( Physics physics)
	{
		for (Spawn.CharSpawn spawn : spawns)
		{
			int cid = spawn.get_cid();
			OtherChar ochar = get_char(cid);

			if (ochar != null)
			{
				// TODO: Blank
			}
			else
			{
				chars.add(spawn.instantiate());
			}
		}

		chars.update(physics);
	}

	public void spawn(Spawn.CharSpawn spawn)
	{
		spawns.add(spawn);
	}

	public void remove(int cid)
	{
		chars.remove(cid);
	}

	public void clear()
	{
		chars.clear();
	}

	public MapObjects  get_chars()
	{
		return chars;
	}

	public  void send_movement(int cid,  LinkedList<Movement> movements)
	{
		OtherChar otherchar = get_char(cid);
		if (otherchar != null)
			otherchar.send_movement(movements);
	}

	public void update_look(int cid,  LookEntry look)
	{
		OtherChar otherchar = get_char(cid);
		if (otherchar != null)
			otherchar.update_look(look);
	}

	OtherChar get_char(int cid)
	{
		return (OtherChar) chars.get(cid);
	}
}
