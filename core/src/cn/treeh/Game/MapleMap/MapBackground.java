package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Physics.MovingObject;
import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxFile;
import cn.treeh.Util.Configure;
import cn.treeh.Util.O;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapBackground {
    enum Type {
        NORMAL,
        HTILED,
        VTILED,
        TILED,
        HMOVEA,
        VMOVEA,
        HMOVEB,
        VMOVEB
    }

    ;

    static Type typebyid(int id) {
        if (id >= 0 && id <= 7)
            return Type.values()[id];
        return Type.NORMAL;
    }

    int WOFFSET, HOFFSET, VWIDTH, VHEIGHT;
    boolean animated;
    Animation animation;
    int cx, cy, htile, vtile;
    double rx, ry;
    float opacity;
    boolean flipped;
    MovingObject movingObject;

    public MapBackground(Node src) {
        VWIDTH = Configure.screenWidth;
        VHEIGHT = Configure.screenHeight;
        WOFFSET = VWIDTH / 2;
        HOFFSET = VHEIGHT / 2;

        Node backsrc = NXFiles.Map().subNode("Back");

        animated = src.subNode("ani").getBool();
        animation = new Animation(backsrc.subNode(src.subNode("bS").getString() + ".img").
                subNode(animated ? "ani" : "back").subNode(src.subNode("no").getString()));
        opacity = src.subNode("a").getInt();
        flipped = src.subNode("f").getBool();
        cx = src.subNode("cx").getInt();
        cy = src.subNode("cy").getInt();
        rx = src.subNode("rx").getInt();
        ry = src.subNode("ry").getInt();
        movingObject = new MovingObject();
        movingObject.set_x(src.subNode("x").getReal());
        movingObject.set_y(src.subNode("y").getReal());

        type = typebyid(src.subNode("type").getInt());

        settype(type);
    }
    Type type;
    void settype(Type type) {

        int dim_x = animation.getDimension()[0];
        int dim_y = animation.getDimension()[1];

        // TODO: Double check for zero. Is this a WZ reading issue?
        if (cx == 0)
            cx = (dim_x > 0) ? dim_x : 1;

        if (cy == 0)
            cy = (dim_y > 0) ? dim_y : 1;

        htile = 1;
        vtile = 1;

        switch (type) {
            case HTILED:
            case HMOVEA:
                htile = VWIDTH / cx + 3;
                break;
            case VTILED:
            case VMOVEA:
                vtile = VHEIGHT / cy + 3;
                break;
            case TILED:
            case HMOVEB:
            case VMOVEB:
                htile = VWIDTH / cx + 3;
                vtile = VHEIGHT / cy + 3;
                break;
        }

        switch (type) {
            case HMOVEA:
            case HMOVEB:
                movingObject.hspeed = rx / 16;
                break;
            case VMOVEA:
            case VMOVEB:
                movingObject.vspeed = ry / 16;
                break;
        }
    }

    public void draw(double viewx, double viewy, float alpha, SpriteBatch batch) {
        double x;

        if (movingObject.hmobile()) {
            x = movingObject.get_absolute_x(viewx, alpha);
        } else {
            double shift_x = rx * (WOFFSET - viewx) / 100 + WOFFSET;
            x = movingObject.get_absolute_x(shift_x, alpha);
        }

        double y;

        if (movingObject.vmobile()) {
            y = movingObject.get_absolute_y(viewy, alpha);
        } else {
            double shift_y = ry * (HOFFSET - viewy) / 100 + HOFFSET;
            y = movingObject.get_absolute_y(shift_y, alpha);
        }

        if (htile > 1) {
            while (x > 0)
                x -= cx;

            while (x < -cx)
                x += cx;
        }

        if (vtile > 1) {
            while (y > 0)
                y -= cy;

            while (y < -cy)
                y += cy;
        }

        int ix = (int) Math.round(x);
        int iy = (int) Math.round(y);

        int tw = cx * htile;
        int th = cy * vtile;
        DrawArg arg = new DrawArg(new int[]{ix, iy}, flipped, opacity / 255);
        for (int tx = 0; tx < tw; tx += cx)
            for (int ty = 0; ty < th; ty += cy)
                animation.draw(arg.addPos(tx, ty), alpha, batch);
    }
}
