package cn.treeh.Game.Physics;

import cn.treeh.NX.Node;

public class Physics {
    FootholdTree fht;
    public Physics(Node src){
        fht = new FootholdTree(src);
    }
    public FootholdTree getFht(){
        return fht;
    }
}
