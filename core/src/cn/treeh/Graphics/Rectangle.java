package cn.treeh.Graphics;

import cn.treeh.NX.Node;

public class Rectangle {
    int[] lt;
    int[] rb;
    public Rectangle(int[] lt, int[] rb){
        this.lt = lt;
        this.rb = rb;
    }
    public Rectangle(int left, int right, int top, int bottom){
        this(new int[]{left, top}, new int[]{right, bottom});
    }
    public Rectangle(Node node){
        lt = node.subNode("lt").getVector();
        rb = node.subNode("rb").getVector();
    }
    public void shift(int[] v){
        lt[0] += v[0];
        lt[1] += v[1];
        rb[0] += v[0];
        rb[1] += v[1];
    }
    public  int left() 
    {
        return lt[0];
    }

    public  int top() 
    {
        return lt[1];
    }

    public  int right() 
    {
        return rb[0];
    }

    public  int bottom() 
    {
        return rb[1];
    }
    public boolean straight()
    {
        return lt[0] == rb[0] && lt[1] == rb[1];
    }
    public boolean empty(){
        return lt[0] == lt[1] && lt[0] == rb[0] && rb[0] == rb[1];
    }
    public boolean contains(int[] v) {
        return !straight() &&
                v[0] >= left() && v[0] <= right() &&
                v[1] >= top() && v[1] <= bottom();
    }
    public boolean overlaps(Rectangle ar){
        return left() <= ar.right() && right() >= ar.left() &&
               bottom() <= ar.top() && top() >= ar.bottom();

    }
}
