package cn.treeh.Util;

public class BoolPair<T> {
    T trueObj, falseObj;
    public BoolPair(){

    }
    public BoolPair(T to, T fo){
        trueObj = to;
        falseObj = fo;
    }
    public void setTrue(T t){
        trueObj = t;
    }
    public void setFalse(T t){
        falseObj = t;
    }
    public T getTrue(){
        return trueObj;
    }
    public T getFalse(){
        return falseObj;
    }
    public T get(boolean b){
        return b ? getTrue() : getFalse();
    }
}