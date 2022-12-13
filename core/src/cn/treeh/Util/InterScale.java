package cn.treeh.Util;

public class InterScale {
    public float before, now;
    public void set(float v) {
        before = now = v;
    }
    public static float lerp(float first, float second, float alpha){
        return alpha <= 0.0f ? first
                : alpha >= 1.0f ? second
                : first == second ? first
                : ((1.0f - alpha) * first + alpha * second);
    }
    public float get(float alpha){
        return lerp(before, now, alpha);
    }
    public InterScale add(float value){
        before = now;
        now += value;
        return this;
    }
}
