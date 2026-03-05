package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Spawn;
import cn.treeh.UI.Component.Component;
import cn.treeh.UI.Component.Cursor;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.LinkedList;

public class MapNPCs{
    MapObjects npcs;
	LinkedList<Spawn.NpcSpawn> spawns = new LinkedList<>();

	public MapNPCs() {

	}

	public void draw(int layer, double viewx, double viewy, float alpha, SpriteBatch batch)
	{
		npcs.draw(layer, viewx, viewy, alpha, batch);
	}

	public void update( Physics physics)
	{
		for (Spawn.NpcSpawn spawn : spawns)
		{

			int oid = spawn.get_oid();
	           MapObject npc = npcs.get(oid);

			if (npc != null)
				npc.makeActive();
			else
				npcs.add(spawn.instantiate(physics));
		}

		npcs.update(physics);
	}

	public void spawn(Spawn.NpcSpawn spawn)
	{
		spawns.add(spawn);
	}

	public void remove(int oid)
	{
		MapObject npc = npcs.get(oid);
		if (npc != null)
			npc.deActivate();
	}

public	void clear()
	{
		npcs.clear();
	}

	public MapObjects get_npcs()
	{
		return npcs;
	}

//	Cursor.State send_cursor(bool pressed, Point<int16_t> position, Point<int16_t> viewpos)
//	{
//		for (MapObjects map_object : npcs)
//		{
//			Npc* npc = static_cast<Npc*>(map_object.second.get());

//			if (npc && npc->is_active() && npc->inrange(position, viewpos))
//			{
//				if (pressed)
//				{
//					// TODO: Try finding dialog first
//					TalkToNPCPacket(npc->get_oid()).dispatch();
//
//					return Cursor::State::IDLE;
//				}
//				else
//				{
//					return Cursor::State::CANCLICK;
//				}
//			}
//		}

//		return Cursor::State::IDLE;
//	}
}
