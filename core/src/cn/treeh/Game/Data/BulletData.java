package cn.treeh.Game.Data;

import cn.treeh.Graphics.Animation;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;

import java.util.TreeMap;

public class BulletData {
	static TreeMap<Integer, BulletData> cache = new TreeMap<>();
    Animation bullet;
	int watk;
    ItemData itemdata;

	public static BulletData getBulletData(int id){
		if(cache.containsKey(id)){
			return cache.get(id);
		} else {
			BulletData bd = new BulletData(id);
			cache.put(id, bd);
			return bd;
		}
	}
    private BulletData(int itemid)
	{
        itemdata = ItemData.getItemData(itemid);
        String prefix = "0" + ((int)(itemid / 10000));
	   String strid = "0" + itemid;
       Node src = NXFiles.Item().subNode("Consume").subNode(prefix + ".img").subNode(strid);

       bullet = new Animation(src.subNode("bullet"));
       watk = src.subNode("info").subNode("incPAD").getInt();
	}

	boolean is_valid()
	{
		return itemdata.is_valid();
	}


	int get_watk()
	{
		return watk;
	}

	 public Animation get_animation()
	{
		return bullet;
	}

	 ItemData get_itemdata()
	{
		return itemdata;
	}
}