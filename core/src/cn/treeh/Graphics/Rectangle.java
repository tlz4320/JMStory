package cn.treeh.Graphics;

import cn.treeh.NX.Node;

public class Rectangle {
    int[] lt;
    int[] rb;
    public Rectangle(Node node){
        lt = node.subNode("lt").getVector();
        rb = node.subNode("rb").getVector();
    }
}
