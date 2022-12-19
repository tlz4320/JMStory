package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Rectangle;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import static cn.treeh.Game.MapleMap.Portal.Type.SPAWN;

public class Portal {
    enum Type {
        SPAWN,
        INVISIBLE,
        REGULAR,
        TOUCH,
        TYPE4,
        TYPE5,
        WARP,
        SCRIPTED,
        SCRIPTED_INVISIBLE,
        SCRIPTED_TOUCH,
        HIDDEN,
        SCRIPTED_HIDDEN,
        SPRING1,
        SPRING2,
        TYPE14
    }


    static Type getType(int id) {
        return Type.values()[id];
    }

    class WarpInfo {
        int mapid;
        String toname;
        String name;
        boolean intramap;
        boolean valid;

        WarpInfo(int m, boolean i, String tn, String n) {
            mapid = m;
            valid = mapid < 999999999;
            intramap = i;
            toname = tn;
            name = n;
        }

        WarpInfo() {
            this(999999999, false, "", "");
        }
    }

    ;

    public Portal(Animation a, Type t, String nm, boolean intramap,
                  int[] p, int tid, String tnm) {
        animation = a;
        type = t;
        name = nm;
        position = p;
        warpinfo = new WarpInfo(tid, intramap, tnm, nm);
        bound = bounds();
        arg = new DrawArg(p);
    }

    public Portal() {
        this(null, SPAWN, "", false, new int[2], 0, "");
    }

    public void update(int[] playerpos) {
        touched = bound.contains(playerpos);
    }

    public void draw(int[] viewpos, float alpha, SpriteBatch batch) {
        if (animation == null || (type == Type.HIDDEN && !touched))
            return;


        animation.draw(arg.addPos(viewpos), alpha, batch);
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    public int[] getPosition() {
        return position;
    }

    public Rectangle bounds() {
        int[] lt = new int[]{position[0] - 25, position[1] - 100};
        int[] rb = new int[]{position[0] + 25, position[1] + 25};
        return new Rectangle(lt, rb);
    }

    WarpInfo getWarpinfo() {
        return warpinfo;
    }

    DrawArg arg;
    Rectangle bound;
    Animation animation;
    Type type;
    String name;
    int[] position;
    WarpInfo warpinfo;
    boolean touched;
}
