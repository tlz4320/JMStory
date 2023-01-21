package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Physics.PhysicsObject;

public class MapObject {
    public MapObject(int oid, int[] position){
        this.oid = oid;
        phobj = new PhysicsObject();
        phobj.set_x(position[0]);
        phobj.set_y(position[1]);
        active = true;
    }

    public PhysicsObject phobj;
    public int oid;
    public boolean active;

    

    public int update( Physics physics)
    {
        physics.move(phobj);

        return phobj.fhlayer;
    }

    public void setPos(int x, int y)
    {
        phobj.set_x(x);
        phobj.set_y(y);
    }



    public void makeActive()
    {
        active = true;
    }

    public void deActivate()
    {
        active = false;
    }

    public boolean isActive()
    {
        return active;
    }

    public int getLayer()
    {
        return phobj.fhlayer;
    }

    public int getOid()
    {
        return oid;
    }

    public int[] getPosition()
    {
        return phobj.get_position();
    }
}
