package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Physics.PhysicsObject;

public class MapObject {
    MapObject(int oid, int[] position){
        this.oid = oid;
        phobj = new PhysicsObject();
        phobj.set_x(position[0]);
        phobj.set_y(position[1]);
        active = true;
    }

    PhysicsObject phobj;
    int oid;
    boolean active;
}
