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
                    Foothold foothold = new Foothold(lastf, id, layer);
                    footholds.put(id, foothold);

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

    public int get_y_below(int[] pos) {
        int fhid = get_fhid_below(pos[0], pos[1]);
        if (fhid != 0)
        {
			Foothold fh = get_fh(fhid);

            return (int)fh.ground_below(pos[0]);
        }
		else
        {
            return borders[1];
        }
    }
    public Foothold get_fh(int id){
        return footholds.get(id);
    }
    public int get_fhid_below(int fx, int fy) {
        int ret = 0;
        double comp = borders[1];

        LinkedList<Integer> range = footholdsbyx.get(fx);
        if(range == null)
            return ret;
        for (int iter : range) {
            Foothold fh = footholds.get(iter);
            double ycomp = fh.ground_below(fx);

            if (comp >= ycomp && ycomp >= fy) {
                comp = ycomp;
                ret = fh.id();
            }
        }

        return ret;
    }
}
