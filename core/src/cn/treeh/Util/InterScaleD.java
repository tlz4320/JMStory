package cn.treeh.Util;

public class InterScaleD {
    public double before, now;
    public void set(double v) {
        before = now = v;
    }
    public static double lerp(double first, double second, double alpha){
        return alpha <= 0.0f ? first
                : alpha >= 1.0f ? second
                : first == second ? first
                : ((1.0f - alpha) * first + alpha * second);
    }
    public double get(double alpha){
        return lerp(before, now, alpha);
    }
    public InterScaleD equal(double value){
        before = now;
        now = value;
        return this;
    }
    public InterScaleD add(double value){
        before = now;
        now += value;
        return this;
    }
    public InterScaleD sub(double value){
        before = now;
        now -= value;
        return this;
    }
    public void normalize()
    {
        before = now;
    }
    public double get()
    {
        return now;
    }
    public double last(){
        return before;
    }
}