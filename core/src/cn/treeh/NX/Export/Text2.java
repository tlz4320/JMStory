package cn.treeh.NX.Export;

import cn.treeh.Game.MapleMap.MapTiles;
import cn.treeh.Game.MapleMap.Obj;
import cn.treeh.Game.MapleMap.Tile;
import cn.treeh.Graphics.Animation;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxFile;
import cn.treeh.Util.O;

import java.util.AbstractMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class Text2 {
    public static void main(String[] args) {
                NxFile map = new NxFile("/Users/tobin/Documents/java_project/JMStory/assets/String.nx");
                Node n = map.getNode();
                for(int i = 0; i < n.nChild();i++){
                    O.ptln(n.subNode(i).getName());
                }

            }
//    public static void main(String[] args) {
//        NxFile map = new NxFile("/Users/tobin/Documents/java_project/JMStory/assets/Map.nx");
//        Node node = map.getNode().subNode("/Map/Map1/100050000.img");
//        node = node.subNode("back");
//        LinkedList<Map.Entry<String, int[]>> list = new LinkedList<>();
//        for (int i = 0; i < node.nChild(); i++) {
//            Node src = node.subNode(i);
//            boolean animated = src.subNode("ani").getBool();
//
//            String id = src.subNode("bS").getString() + ".img" + "_" +
//                    (animated ? "ani" : "back") + "_" + src.subNode("no").getString();
//            int opacity = src.subNode("a").getInt();
//            boolean flipped = src.subNode("f").getBool();
//            int cx = src.subNode("cx").getInt();
//            int cy = src.subNode("cy").getInt();
//            int rx = src.subNode("rx").getInt();
//            int ry = src.subNode("ry").getInt();
//            int x = (int) src.subNode("x").getReal();
//            int y = (int) src.subNode("y").getReal();
//            list.add(new AbstractMap.SimpleEntry<>(id, new int[]{cx, cy, rx, ry, x, y, src.subNode("type").getInt()}));
//        }
//        O.ptln("test");
//    }

}
