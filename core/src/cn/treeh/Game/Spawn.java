package cn.treeh.Game;

import cn.treeh.Game.MapleMap.*;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Player.Look.CharLook;
import cn.treeh.Game.Player.OtherChar;
import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.Texture;
import cn.treeh.IO.Net.LookEntry;

public class Spawn {
    public static class NpcSpawn
	{
		public NpcSpawn(int oid, int npcid, int[] position, boolean mirrored, int fh){
            this.oid = oid;
            this.id = npcid;
            this.position = position;
            this.flip = mirrored;
            this.fh=fh;
        }

		public int get_oid(){
            return oid;
        }
		public MapObject instantiate(Physics physics){
            int[] spawnposition = physics.get_y_below(position);
		return new Npc(id, oid, flip, fh, false, spawnposition);
        }

		int oid;
		int id;
	int[] position;
		boolean flip;
		int fh;
	};

	static public class MobSpawn
	{
		public MobSpawn(int oid, int id, int mode, int stance, int fh, boolean newspawn, int team, int[] position){
			this.oid = oid;
			this.id = id;
			this.mode = mode;
			this.stance = stance;
			this.fh = fh;
			this.newspawn = newspawn;
			this.team = team;
			this.position = position;
		}

		public int get_mode(){
            return mode;
        }
		public int get_oid(){
            return oid;
        }
		public MapObject instantiate(){
			return new Mob(oid, id, mode, stance, fh, newspawn, team, position);
		}

		int oid;
		int id;
		int mode;
		int stance;
		int fh;
		boolean newspawn;
		int team;
	int[] position;
	};

	public static class ReactorSpawn
	{
		ReactorSpawn(int oid, int rid, int state, int[] position){
			this.oid  = oid;
			this.rid = rid;
			this.state = state;
			this.position = position;
		}

		int get_oid(){
			return oid;
		}
		MapObject instantiate( Physics physics){
			int[] spawnposition = physics.get_y_below(position);
			return new Reactor(oid, rid, state, spawnposition);
		}

		int oid;
		int rid;
		int state;
	int[] position;
	};

	public static class DropSpawn
	{
		public DropSpawn(int oid, int id, boolean meso, int owner, int[] position,
				  int[] destination, int droptype, int mode, boolean playerdrop){
			this.oid = oid;
			this.id = id;
			this.meso = meso;
			this.owner = owner;
			this.start = position;
			this.dest = destination;
			this.droptype = droptype;
			this.mode = mode;
			this.playerdrop = playerdrop;
		}

		public boolean is_meso(){
			return meso;
		}
		public int get_itemid(){
			return id;
		}
		public 	int get_oid(){
			return oid;
		}
//		public MapObject instantiate(Animation icon){
//			return new MesoDrop(oid, owner, start, dest, droptype, mode, playerdrop, icon);
//		}
		public MapObject instantiate(Texture icon){
			return new ItemDrop(oid, owner, start, dest, droptype, mode, id, playerdrop, icon);
		}

		int oid;
		int id;
		boolean meso;
		int owner;
	int[] start;
	int[] dest;
		int droptype;
		int mode;
		boolean playerdrop;
	};

	public static class CharSpawn
	{
	public 	CharSpawn(int cid, LookEntry look, int level, int job, String name, int stance, int[] position){
			this.cid =cid;
			this.look = look;
			this.level = level;
			this.job = job;
			this.name = name;
			this.stance = stance;
			this.position = position;
		}

		public int get_cid(){
return cid;
		}
		public MapObject instantiate(){
		return new OtherChar(cid, new CharLook(look), level, job, name, stance, position);
	}

		int cid;
		int level;
		int job;
	String name;
		int stance;
	int[] position;
		LookEntry look;
	};
}
