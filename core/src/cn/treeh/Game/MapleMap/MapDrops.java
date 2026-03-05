package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Data.ItemData;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Spawn;
import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.Sprite;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class MapDrops {
    enum MesoIcon
        {
            BRONZE,
            GOLD,
            BUNDLE,
            BAG,
            NUM_ICONS
        };
    MapObjects drops;
    Animation[] mesoicons = new Animation[MesoIcon.NUM_ICONS.ordinal()];
    boolean lootenabled;

    LinkedList<Spawn.DropSpawn> spawns = new LinkedList<>();
    public void init() {
        Node src = NXFiles.Item().subNode("Special/0900.img");

        mesoicons[MesoIcon.BRONZE.ordinal()] = new Animation(src.subNode("09000000/iconRaw"));
        mesoicons[MesoIcon.GOLD.ordinal()] = new Animation(src.subNode("09000001/iconRaw"));
        mesoicons[MesoIcon.BUNDLE.ordinal()] = new Animation(src.subNode("09000002/iconRaw"));
        mesoicons[MesoIcon.BAG.ordinal()] = new Animation(src.subNode("09000003/iconRaw"));
    }
    public void  draw(int layer, double viewx, double viewy, float alpha, SpriteBatch batch)
    {
        drops.draw(layer, viewx, viewy, alpha, batch);
    }

    public void  update( Physics physics)
    {

        for (Spawn.DropSpawn spawn : spawns)
        {

            int oid = spawn.get_oid();
            MapObject drop = drops.get(oid);
            if (drop != null)
            {
                drop.makeActive();
            }
            else
            {
                int itemid = spawn.get_itemid();
                boolean meso = spawn.is_meso();

                if (meso)
                {
                    MesoIcon mesotype = (itemid > 999)
						? MesoIcon.BAG : (itemid > 99)
						? MesoIcon.BUNDLE : (itemid > 49)
						? MesoIcon.GOLD : MesoIcon.BRONZE;

                    Animation icon = mesoicons[mesotype.ordinal()];
//                    drops.add(spawn.instantiate(icon));
                }
                else
                {
                    ItemData itemdata = ItemData.getItemData(itemid);
                    if(itemdata != null){
                        Texture icontmp = itemdata.get_icon(true);
                        drops.add(spawn.instantiate(icontmp));
                    }

                }
            }
        }

        for (Animation mesoicon : mesoicons)
            mesoicon.update();

        drops.update(physics);

        lootenabled = true;
    }

    public void  spawn(Spawn.DropSpawn spawn)
    {
        spawns.add(spawn);
    }

    public void  remove(int oid, int mode,  PhysicsObject looter)
    {
        MapObject drop = drops.get(oid);
        if (drop instanceof Drop)
            ((Drop)drop).expire(mode, looter);
    }

    public void  clear()
    {
        drops.clear();
    }



    public Map.Entry<Integer, int[]> find_loot_at(int[] playerpos)
    {
        if (!lootenabled)
            return new AbstractMap.SimpleEntry<>(0, new int[]{0,0});

        for (Map.Entry<Integer, MapObject> en : drops.objects.entrySet())
        {
            MapObject drop = en.getValue();

            if (drop instanceof Drop && ((Drop)drop).bounds().contains(playerpos))
            {
                lootenabled = false;

                int oid = en.getKey();
                int[] position = drop.getPosition();

                return new AbstractMap.SimpleEntry<>( oid, position );
            }
        }

        return new AbstractMap.SimpleEntry<>(0, new int[]{0,0});
    }
}
