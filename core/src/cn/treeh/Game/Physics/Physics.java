package cn.treeh.Game.Physics;

import cn.treeh.NX.Node;

public class Physics {
    double GRAVFORCE = 0.14;
    double SWIMGRAVFORCE = 0.03;
    double FRICTION = 0.5;
    double SLOPEFACTOR = 0.1;
    double GROUNDSLIP = 3.0;
    double FLYFRICTION = 0.05;
    double SWIMFRICTION = 0.08;
    FootholdTree fht;

    public Physics(Node src) {
        fht = new FootholdTree(src);
    }

    public FootholdTree getFht() {
        return fht;
    }

    public int[] get_y_below(int[] pos) {
        int ground = fht.get_y_below(pos);
        return new int[]{pos[0], ground - 1};
    }

    void move_normal(PhysicsObject phobj) {
        phobj.vacc = 0.0;
        phobj.hacc = 0.0;

        if (phobj.onground) {
            phobj.vacc += phobj.vforce;
            phobj.hacc += phobj.hforce;

            if (phobj.hacc == 0.0 && phobj.hspeed < 0.1 && phobj.hspeed > -0.1) {
                phobj.hspeed = 0.0;
            } else {
                double inertia = phobj.hspeed / GROUNDSLIP;
                double slopef = phobj.fhslope;

                if (slopef > 0.5)
                    slopef = 0.5;
                else if (slopef < -0.5)
                    slopef = -0.5;

                phobj.hacc -= (FRICTION + SLOPEFACTOR * (1.0 + slopef * -inertia)) * inertia;
            }
        } else if (phobj.is_flag_not_set(PhysicsObject.Flag.NOGRAVITY)) {
            phobj.vacc += GRAVFORCE;
        }

        phobj.hforce = 0.0;
        phobj.vforce = 0.0;

        phobj.hspeed += phobj.hacc;
        phobj.vspeed += phobj.vacc;
    }

    void move_flying(PhysicsObject phobj) {
        phobj.hacc = phobj.hforce;
        phobj.vacc = phobj.vforce;
        phobj.hforce = 0.0;
        phobj.vforce = 0.0;

        phobj.hacc -= FLYFRICTION * phobj.hspeed;
        phobj.vacc -= FLYFRICTION * phobj.vspeed;

        phobj.hspeed += phobj.hacc;
        phobj.vspeed += phobj.vacc;

        if (phobj.hacc == 0.0 && phobj.hspeed < 0.1 && phobj.hspeed > -0.1)
            phobj.hspeed = 0.0;

        if (phobj.vacc == 0.0 && phobj.vspeed < 0.1 && phobj.vspeed > -0.1)
            phobj.vspeed = 0.0;
    }

    void move_swimming(PhysicsObject phobj) {
        phobj.hacc = phobj.hforce;
        phobj.vacc = phobj.vforce;
        phobj.hforce = 0.0;
        phobj.vforce = 0.0;

        phobj.hacc -= SWIMFRICTION * phobj.hspeed;
        phobj.vacc -= SWIMFRICTION * phobj.vspeed;

        if (phobj.is_flag_not_set(PhysicsObject.Flag.NOGRAVITY))
            phobj.vacc += SWIMGRAVFORCE;

        phobj.hspeed += phobj.hacc;
        phobj.vspeed += phobj.vacc;

        if (phobj.hacc == 0.0 && phobj.hspeed < 0.1 && phobj.hspeed > -0.1)
            phobj.hspeed = 0.0;

        if (phobj.vacc == 0.0 && phobj.vspeed < 0.1 && phobj.vspeed > -0.1)
            phobj.vspeed = 0.0f;
    }

    public void move(PhysicsObject phobj) {
//         Determine which platform the object is currently on
        fht.update_fh(phobj);

        // Use the appropriate physics for the terrain the object is on
        switch (phobj.type) {
            case NORMAL:
                move_normal(phobj);
                fht.limit_movement(phobj);
                break;
            case FLYING:
                move_flying(phobj);
                fht.limit_movement(phobj);
                break;
            case SWIMMING:
                move_swimming(phobj);
                fht.limit_movement(phobj);
                break;
            case FIXATED:
            default:
                break;
        }

        // Move the object forward
        phobj.move();
    }
}
