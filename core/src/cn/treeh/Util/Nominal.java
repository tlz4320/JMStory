package cn.treeh.Util;

public class Nominal<T> {
    T before, now;
    float threshold;
    public T get(){
        return now;
    }
    public T get(float a){
        return a > threshold ? now : before;
    }
    public T last(){
        return before;
    }
    public void set(T v){
        before = now = v;
    }
    public void normalize(){
        before = now;
    }
    public boolean normalized(){
        return before == now;
    }
    public void next(T value, float t){
        before = now;
        now = value;
        threshold = t;
    }
}
