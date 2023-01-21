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
        if (fhid != 0) {
            Foothold fh = get_fh(fhid);

            return (int) fh.ground_below(pos[0]);
        } else {
            return borders[1];
        }
    }
    Foothold empty = new Foothold();
    public Foothold get_fh(int id) {
        Foothold res = footholds.get(id);
        return res == null ? empty : res;
    }

    public int get_fhid_below(double fx, double fy) {
        return get_fhid_below((int) fx, (int) fy);
    }

    public int get_fhid_below(int fx, int fy) {
        int ret = 0;
        double comp = borders[1];

        LinkedList<Integer> range = footholdsbyx.get(fx);
        if (range == null)
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

    public void update_fh(PhysicsObject phobj) {
        if (phobj.type == PhysicsObject.Type.FIXATED && phobj.fhid > 0)
            return;

        Foothold curfh = get_fh(phobj.fhid);
        boolean checkslope = false;

        double x = phobj.crnt_x();
        double y = phobj.crnt_y();

        if (phobj.onground) {
            if (Math.floor(x) > curfh.r())
                phobj.fhid = curfh.next();
            else if (Math.ceil(x) < curfh.l())
                phobj.fhid = curfh.prev();

            if (phobj.fhid == 0)
                phobj.fhid = get_fhid_below(x, y);
            else
                checkslope = true;
        } else {
            phobj.fhid = get_fhid_below(x, y);

            if (phobj.fhid == 0)
                return;
        }

        Foothold nextfh = get_fh(phobj.fhid);
        phobj.fhslope = nextfh.slope();

        double ground = nextfh.ground_below(x);

        if (phobj.vspeed == 0.0 && checkslope) {
            double vdelta = Math.abs(phobj.fhslope);

            if (phobj.fhslope < 0.0)
                vdelta *= (ground - y);
            else if (phobj.fhslope > 0.0)
                vdelta *= (y - ground);

            if (curfh.slope() != 0.0 || nextfh.slope() != 0.0) {
                if (phobj.hspeed > 0.0 && vdelta <= phobj.hspeed)
                    phobj.y.equal(ground);
                else if (phobj.hspeed < 0.0 && vdelta >= phobj.hspeed)
                    phobj.y.equal(ground);
            }
        }

        phobj.onground = phobj.y.get() == ground;

        if (phobj.enablejd || phobj.is_flag_set(PhysicsObject.Flag.CHECKBELOW)) {
            int belowid = get_fhid_below(x, nextfh.ground_below(x) + 1.0);

            if (belowid > 0) {
                double nextground = get_fh(belowid).ground_below(x);
                phobj.enablejd = (nextground - ground) < 600.0;
                phobj.groundbelow = ground + 1.0;
            } else {
                phobj.enablejd = false;
            }

            phobj.clear_flag(PhysicsObject.Flag.CHECKBELOW);
        }

        if (phobj.fhlayer == 0 || phobj.onground)
            phobj.fhlayer = nextfh.layer();

        if (phobj.fhid == 0) {
            phobj.fhid = curfh.id();
            phobj.limitx(curfh.x1());
        }
    }

    double get_wall(int curid, boolean left, double fy) {
        int shorty = (int) fy;
        int[] vertical = new int[]{shorty - 50, shorty - 1};
        Foothold cur = get_fh(curid);

        if (left) {
            Foothold prev = get_fh(cur.prev());

            if (prev.is_blocking(vertical))
                return cur.l();

            Foothold prev_prev = get_fh(prev.prev());

            if (prev_prev.is_blocking(vertical))
                return prev.l();

            return walls[0];
        } else {
            Foothold next = get_fh(cur.next());

            if (next.is_blocking(vertical))
                return cur.r();

            Foothold next_next = get_fh(next.next());

            if (next_next.is_blocking(vertical))
                return next.r();

            return walls[1];
        }
    }

    double get_edge(int curid, boolean left) {
        Foothold fh = get_fh(curid);

        if (left) {
            int previd = fh.prev();

            if (previd <= 0)
                return fh.l();

            Foothold prev = get_fh(previd);
            int prev_previd = prev.prev();

            if (prev_previd <= 0)
                return prev.l();

            return walls[0];
        } else {
            int nextid = fh.next();

            if (nextid <= 0)
                return fh.r();

            Foothold next = get_fh(nextid);
            int next_nextid = next.next();

            if (next_nextid <= 0)
                return next.r();

            return walls[1];
        }
    }

    public void limit_movement(PhysicsObject phobj) {
        if (phobj.hmobile()) {
            double crnt_x = phobj.crnt_x();
            double next_x = phobj.next_x();

            boolean left = phobj.hspeed < 0.0f;
            double wall = get_wall(phobj.fhid, left, phobj.next_y());
            boolean collision = left ? crnt_x >= wall && next_x <= wall : crnt_x <= wall && next_x >= wall;

            if (!collision && phobj.is_flag_set(PhysicsObject.Flag.TURNATEDGES))
            {
                wall = get_edge(phobj.fhid, left);
                collision = left ? crnt_x >= wall && next_x <= wall : crnt_x <= wall && next_x >= wall;
            }

            if (collision) {
                phobj.limitx(wall);
                phobj.clear_flag(PhysicsObject.Flag.TURNATEDGES);
            }
        }

        if (phobj.vmobile()) {
            double crnt_y = phobj.crnt_y();
            double next_y = phobj.next_y();

            double[] ground = new double[]{
                get_fh(phobj.fhid).ground_below(phobj.crnt_x()),
                        get_fh(phobj.fhid).ground_below(phobj.next_x())
            };

            boolean collision = crnt_y <= ground[0] && next_y >= ground[1];

            if (collision) {
                phobj.limity(ground[1]);

                limit_movement(phobj);
            } else {
                if (next_y < borders[0])
                    phobj.limity(borders[0]);
                else if (next_y > borders[1])
                    phobj.limity(borders[1]);
            }
        }
    }
}
