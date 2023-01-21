package cn.treeh.Game.Physics;

public class PhysicsObject extends MovingObject{
    // Determines which physics engine to use
    public enum Type
    {
        NORMAL,
        ICE,
        SWIMMING,
        FLYING,
        FIXATED
    };

    public enum Flag
    {
        USELESS1,
        NOGRAVITY,
        TURNATEDGES,
        USELESS,
        CHECKBELOW
    };

    public  Type type = Type.NORMAL;
    public int flags = 0;
    public int fhid = 0;
    public double fhslope = 0.0;
    public int fhlayer = 0;
    public double groundbelow = 0.0;
    public boolean onground = true;
    public boolean enablejd = false;

    public double hforce = 0.0;
    public double vforce = 0.0;
    public double hacc = 0.0;
    public double vacc = 0.0;

    public boolean is_flag_set(Flag f)
    {
        return (flags & f.ordinal()) != 0;
    }

    public boolean is_flag_not_set(Flag f)
    {
        return !is_flag_set(f);
    }

    public void set_flag(Flag f)
    {
        flags |= f.ordinal();
    }

    public void clear_flag(Flag f)
    {
        flags &= ~f.ordinal();
    }

    public  void clear_flags()
    {
        flags = 0;
    }
}
