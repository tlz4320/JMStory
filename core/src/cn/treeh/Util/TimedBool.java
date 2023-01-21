package cn.treeh.Util;

public class TimedBool {

    public TimedBool() {
        value = false;
        delay = 0;
        last = 0;
    }

    public boolean get() {
        return value;
    }

    public void setTime(int millis) {
        last = millis;
        delay = millis;
        value = true;
    }

    public void update() {
        update(Configure.TIME_STEP);
    }

    public void update(int timestep) {
        if (value) {
            if (timestep >= delay) {
                value = false;
                delay = 0;
            } else {
                delay -= timestep;
            }
        }
    }

    public void set(boolean b) {
        value = b;
        delay = 0;
        last = 0;
    }


    public float alpha() {
        return 1.0f - (float) delay / last;
    }

    int last;
    int delay;
    boolean value;

}
