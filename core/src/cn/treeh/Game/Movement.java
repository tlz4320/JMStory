package cn.treeh.Game;

import cn.treeh.Game.Physics.PhysicsObject;

public class Movement {
    public 	enum Type
            {
                NONE,
                ABSOLUTE,
                RELATIVE,
                CHAIR,
                JUMPDOWN
            }
    public Movement(Type t, int c, int x, int y, int lx, int ly, int f, int s, int d){
        type = t;
        command = c;
        xpos = x;
        ypos = y;
        lastx = lx;
        lasty = ly;
        fh = f;
        newstate = s;
        duration = d;
    }
    public Movement(int x, int y, int lx, int ly, int s, int d){
        this(Type.ABSOLUTE, 0, x, y, lx, ly, 0, s, d);
    }
    public	Movement(PhysicsObject phobj, int s){
        this(Type.ABSOLUTE, 0,phobj.get_x(), phobj.get_y(), phobj.get_last_x(), phobj.get_last_y(), phobj.fhid, s, 1);
    }
    public Movement(){
        this(Type.NONE, 0, 0, 0, 0, 0, 0, 0, 0);
    }

    public boolean hasmoved(Movement newmove)
    {
        return newmove.newstate != newstate || newmove.xpos != xpos || newmove.ypos != ypos || newmove.lastx != lastx || newmove.lasty != lasty;
    }

    Type type;
    int command;
    public int xpos;
    public int ypos;
    int lastx;
    int lasty;
    int fh;
    public int newstate;
    int duration;
}