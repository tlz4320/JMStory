package cn.treeh.Game.Physics;

import cn.treeh.NX.Node;

public class Physics {
    FootholdTree fht;
    public Physics(Node src){
        fht = new FootholdTree(src);
    }
    public FootholdTree getFht(){
        return fht;
    }
    public int[] get_y_below(int[] pos){
        int ground = fht.get_y_below(pos);
        return new int[]{pos[0], ground - 1};
    }
    public void move(PhysicsObject obj){
        // Determine which platform the object is currently on
//        fht.update_fh(phobj);
//
//        // Use the appropriate physics for the terrain the object is on
//        switch (phobj.type)
//        {
//            case PhysicsObject::Type::NORMAL:
//            move_normal(phobj);
//                fht.limit_movement(phobj);
//                break;
//            case PhysicsObject::Type::FLYING:
//            move_flying(phobj);
//                fht.limit_movement(phobj);
//                break;
//            case PhysicsObject::Type::SWIMMING:
//            move_swimming(phobj);
//                fht.limit_movement(phobj);
//                break;
//            case PhysicsObject::Type::FIXATED:
//            default:
//                break;
//        }
//
//        // Move the object forward
//        phobj.move();
    }
}
