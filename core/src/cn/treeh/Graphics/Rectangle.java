package cn.treeh.Graphics;

import cn.treeh.NX.Node;

public class Rectangle {
    int[] lt;
    int[] rb;
    public Rectangle(int[] lt, int[] rb){
        this.lt = lt;
        this.rb = rb;
    }
    public Rectangle(Node node){
        lt = node.subNode("lt").getVector();
        rb = node.subNode("rb").getVector();
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
    public boolean contains(int[] v) {
        return !straight() &&
                v[0] >= left() && v[0] <= right() &&
                v[1] >= top() && v[1] <= bottom();
    }
}
