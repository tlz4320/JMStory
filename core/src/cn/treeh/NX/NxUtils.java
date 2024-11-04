package cn.treeh.NX;

import cn.treeh.Util.StringUtil;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;

public class NxUtils {
    public static Node getMapNode(int mapid) {
        int prefix = mapid / 100000000;


        return NXFiles.Map().subNode("Map")
                .subNode("Map" + prefix).subNode(StringUtil.extend(mapid, 9) + ".img");
    }

    public static HashMap<Integer, Map.Entry<String, String>> getMapLife(int mapid) {
        HashMap<Integer, Map.Entry<String, String>> map_life = new HashMap<>();

        Node portal = getMapNode(mapid);
        Node lifeNode = portal.subNode("life");
        for (int i = 0; i < lifeNode.nChild(); i++) {
            Node life = lifeNode.subNode(i);
            int life_id = life.subNode("id").getInt();
            String life_type = life.subNode("type").getString();
            int hide_life = life.subNode("hide").getInt();

            if (hide_life == 0) {
                if (life_type.equals("m")) {
                    // Mob
                    Node life_name = NXFiles.String().subNode("Mob.img/" + life_id + "/name");

                    String life_id_str = StringUtil.extend(life_id, 7);
                    Node life_level = NXFiles.Mob().subNode(life_id_str + ".img").subNode("info/level");

                    if (life_name.valid() && life_level.valid())
                        map_life.put(life_id, new AbstractMap.SimpleEntry<>(life_type,
                                life_name.getString() + "(Lv. " + life_level.getString() + ")"));
                } else if (life_type.equals("n")) {
                    // NPC
                    Node life_name = NXFiles.String().subNode("Npc.img/" + life_id + "/name");
                    if (life_name.valid())
                        map_life.put(life_id, new AbstractMap.SimpleEntry<>(life_type, life_name.getString()));
                }
            }
        }

        return map_life;
    }
}
