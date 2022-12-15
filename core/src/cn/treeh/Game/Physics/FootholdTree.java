package cn.treeh.Game.Physics;

import cn.treeh.NX.Node;

import java.util.LinkedList;
import java.util.TreeMap;

public class FootholdTree {
    TreeMap<Integer, Foothold> footholds;
    TreeMap<Integer, LinkedList<Integer>> footholdsbyx;

    int[] walls, borders;

    public FootholdTree(Node src) {
        footholds = new TreeMap<>();
        footholdsbyx = new TreeMap<>();
        int leftw = 30000;
        int rightw = -30000;
        int botb = -30000;
        int topb = 30000;

        for (int i = 0; i < src.nChild(); i++) {
            Node basef = src.subNode(i);
            int layer;

            try {
                layer = Integer.parseInt(basef.getName());
            } catch (NumberFormatException e) {
                System.err.println(e);
                continue;
            }

            for (int j = 0; j < basef.nChild(); j++) {
                Node midf = basef.subNode(j);
                for (int k = 0; k < midf.nChild(); k++) {
                    Node lastf = midf.subNode(k);
                    int id;
                    try {
                        id = Integer.parseInt(lastf.getName());
                    } catch (NumberFormatException e) {
                        System.err.println(e);
                        continue;
                    }
                    Foothold foothold = footholds.put(id, new Foothold(lastf, id, layer));

                    if (foothold.l() < leftw)
                        leftw = foothold.l();

                    if (foothold.r() > rightw)
                        rightw = foothold.r();

                    if (foothold.b() > botb)
                        botb = foothold.b();

                    if (foothold.t() < topb)
                        topb = foothold.t();

                    if (foothold.is_wall())
                        continue;

                    int start = foothold.l();
                    int end = foothold.r();

                    for (int t = start; t <= end; t++) {
                        LinkedList<Integer> tmp = footholdsbyx.getOrDefault(t, new LinkedList<>());
                        tmp.add(id);
                        footholdsbyx.put(t, tmp);
                    }
                }
            }
        }

        walls = new int[]{leftw + 25, rightw - 25};
        borders = new int[]{topb - 300, botb + 100};
    }

    public int[] getWalls() {
        return walls;
    }

    public int[] getBorders() {
        return borders;
    }
}
