package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.Animation;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxFile;

import java.util.HashMap;
import java.util.TreeMap;

public class MapPortals {
    TreeMap<Integer, Portal> portals;
    HashMap<String, Integer> name2id;
    static int WARPCD = 48;
    int cooldown;
    Animation[] animations = new Animation[15];
    public MapPortals(Node src, int mapId){
        name2id = new HashMap<>();
        portals = new TreeMap<>();
        Node sub;
        for (int i = 0; i < src.nChild(); i++) {
            sub = src.subNode(i);
            int id = -1;
            try {
                id = Integer.parseInt(sub.getName());
            } catch (NumberFormatException ignored) {
                continue;
            }

            Portal.Type type = Portal.getType(sub.subNode("pt").getInt());
            String name = sub.subNode("pn").getString();
            String target_name = sub.subNode("tn").getString();
            int target_id = sub.subNode("tm").getInt();
            int[] position = new int[]{sub.subNode("x").getInt(), sub.subNode("y").getInt()};

            Animation animation = animations[type.ordinal()];
            boolean intramap = target_id == mapId;
            portals.put(id, new Portal(animation, type, name, intramap, position, target_id, target_name));

            name2id.put(name, id);
        }
        cooldown = WARPCD;
    }
    public void init(){
        Node src = NXFiles.Map().subNode("MapHelper.img/portal/game");

        animations[Portal.Type.HIDDEN.ordinal()] =
                new Animation(src.subNode("ph/default/portalContinue"));
        animations[Portal.Type.REGULAR.ordinal()] =
                new Animation(src.subNode("pv"));
    }

    public int[] getPortalByID(int id){
        Portal iter = portals.get(id);

        if (iter != null)
        {
            int[] res = iter.getPosition();
            return new int[]{res[0], res[1] - 30};
        }
        else
        {
            return new int[2];
        }
    }
}
