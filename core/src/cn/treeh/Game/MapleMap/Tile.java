package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile {
    Texture texture;
    DrawArg arg;
    int[] pos;
    int z;
    public Tile(Node src, String ts){
        Node dsrc = NXFiles.Map().subNode("Tile").subNode(ts).
                subNode(src.subNode("u").getString("")).
                subNode(src.subNode("no").getString(""));
        texture = new Texture(dsrc);
        pos = new int[]{src.subNode("x").getInt(), src.subNode("y").getInt()};
        arg = new DrawArg(pos);
        z = dsrc.subNode("z").getInt();
        if(z == 0)
            z = dsrc.subNode("zM").getInt();
    }
    public void draw(int[] pos, SpriteBatch batch){
        texture.draw(arg.addPos(pos), batch);
    }
    public int getZ(){
        return z;
    }
}
