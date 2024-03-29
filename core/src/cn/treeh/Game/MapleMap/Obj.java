package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxFile;
import cn.treeh.Util.O;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Obj {
    Animation animation;
    int[] pos;
    boolean flip;
    int z;
    DrawArg arg;
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
        arg = new DrawArg(pos, flip);
    }
    public  int getZ(){
        return z;
    }
    public void draw(int[] pos, float alpha, SpriteBatch batch){
        animation.draw(arg.addPos(pos),alpha, batch);
    }
    public void update(){
        animation.update();
    }
}
