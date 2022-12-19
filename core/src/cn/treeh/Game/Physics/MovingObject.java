package cn.treeh.Game.Physics;

import cn.treeh.Util.Configure;
import cn.treeh.Util.InterScaleD;

public class MovingObject {
    public InterScaleD x = new InterScaleD();
    public InterScaleD y = new InterScaleD();
    public double hspeed = 0.0;
    public double vspeed = 0.0;

    public void normalize() {
        x.normalize();
        y.normalize();
    }

    public void move() {
        x.add(hspeed);
        y.add(vspeed);
    }

    public void set_x(double d) {
        x.set(d);
    }

    public void set_y(double d) {
        y.set(d);
    }

    public void limitx(double d) {
        x.equal(d);
        hspeed = 0.0;
    }

    public void limity(double d) {
        y.equal(d);
        vspeed = 0.0;
    }

    public void movexuntil(double d, int delay) {
        if (delay != 0) {
            double hdelta = d - x.get();
            hspeed = Configure.TIME_STEP * hdelta / delay;
        }
    }

    public void moveyuntil(double d, int delay) {
        if (delay != 0) {
            double vdelta = d - y.get();
            vspeed = Configure.TIME_STEP * vdelta / delay;
        }
    }

    public boolean hmobile() {
        return hspeed != 0.0;
    }

    public boolean vmobile() {
        return vspeed != 0.0;
    }

    public boolean mobile() {
        return hmobile() || vmobile();
    }

    public double crnt_x() {
        return x.get();
    }

    public double crnt_y() {
        return y.get();
    }

    public double next_x() {
        return x.get() + hspeed;
    }

    public double next_y() {
        return y.get() + vspeed;
    }

    public int get_x() {
        double rounded = Math.round(x.get());
        return (int) rounded;
    }

    public int get_y() {
        double rounded = Math.round(y.get());
        return (int) rounded;
    }

    public int get_last_x() {
        double rounded = Math.round(x.last());
        return (int) (rounded);
    }

    public int get_last_y() {
        double rounded = Math.round(y.last());
        return (int) (rounded);
    }

    public int[] get_position() {
        return new int[]{get_x(), get_y()};
    }

    public int get_absolute_x(double viewx, float alpha) {
        double interx = x.before == x.now ? Math.round(x.get()) : x.get(alpha);

        return (int) Math.round(interx + viewx);
    }

    public int get_absolute_y(double viewy, float alpha) {
        double intery = y.before == y.now ? Math.round(y.get()) : y.get(alpha);

        return (int) Math.round(intery + viewy);
    }

    public int[] get_absolute(double viewx, double viewy, float alpha) {
        return new int[]{get_absolute_x(viewx, alpha), get_absolute_y(viewy, alpha)};
    }
}