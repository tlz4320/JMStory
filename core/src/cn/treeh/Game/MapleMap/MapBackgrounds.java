package cn.treeh.Game.MapleMap;

import cn.treeh.NX.Node;

import java.util.LinkedList;

public class MapBackgrounds {
    LinkedList<MapBackground> foregrounds = new LinkedList<>(),
            backgrounds = new LinkedList<>();
    boolean black;

    public MapBackgrounds(Node src) {
        int no = 0;
        Node back = src.subNode("" + no);

        while (back.size() > 0) {
            boolean front = back.subNode("front").getBool();

            if (front)
                foregrounds.add(new MapBackground(back));
            else
                backgrounds.add(new MapBackground(back));

            no++;
            back = src.subNode("" + no);
        }
        black = src.subNode("0/bS").getString().length() == 0;
    }
}
