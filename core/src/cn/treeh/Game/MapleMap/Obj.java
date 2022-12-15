package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.Animation;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxFile;

public class Obj {
    Animation animation;
    int[] pos;
    boolean flip;
    int z;
    public Obj(Node src)
    {
        animation = new Animation(NXFiles.Map().subNode("Obj").
                subNode(src.subNode("oS").getString() + ".img").
                subNode(src.subNode("l0").getString()).
                subNode(src.subNode("l1").getString()).
                subNode(src.subNode("l2").getString()));
        pos = new int[]{src.subNode("x").getInt(), src.subNode("y").getInt()};
        flip = src.subNode("f").getBool();
        z = src.subNode("z").getInt();
    }
    public  int getZ(){
        return z;
    }
}
