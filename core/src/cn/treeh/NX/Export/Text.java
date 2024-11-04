package cn.treeh.NX;

import cn.treeh.Game.MapleMap.Obj;
import cn.treeh.Game.MapleMap.Tile;
import cn.treeh.Util.O;
import com.badlogic.gdx.graphics.Pixmap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.TreeMap;

public class Text{
    public static void main(String[] args) throws Exception{
        NxFile map = new NxFile("/Users/tobin/Documents/java_project/JMStory/assets/Map.nx");
        HashMap<String, Integer> acc = new HashMap<>();
        Node node = map.getNode().subNode("Map");
        HashSet<String> a = new HashSet<>();
        for (int i = 0; i < node.nChild(); i++) {
            Node maps = node.subNode(i);
            for (int j = 0; j < maps.nChild(); j++) {
                Node src = maps.subNode(j);
                for (int layer = 0; layer < 8; layer++) {
                    Node l = src.subNode("" + layer);
                    String tileset = l.subNode("info/tS").getString();
                    tileset += ".img";
                    Node tile = l.subNode("tile");
                    if(tile.nChild() != 0)
                        a.add(src.getName());
                    for (int k = 0; k < tile.nChild(); k++) {
                        Node tilenode = tile.subNode(i);
                        String id = tileset + "_" + tilenode.subNode("u").getString("")  +
                                "_" + tilenode.subNode("no").getString("");
                        acc.put(id, acc.getOrDefault(id, 0) + 1);
                    }
                }
            }
        }
        O.ptln("test");
    }
}
