package cn.treeh.Graphics;

import cn.treeh.NX.Bitmap;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Texture {
    Bitmap bitmap;
    public int[] origin;
    public int[] dimensions;

    TextureRegion region;
    public Texture(Node src){
        if(src.getType() == Node.Type.bitmap){
            String link = src.subNode("source").getString("");
            if(!link.equals("") && !src.isRoot()){
                src = src.getRoot().subNode(link.substring(link.indexOf('/') + 1));
            }
            bitmap = src.getBitmap();
            origin = src.subNode("origin").getVector();
            dimensions = new int[]{bitmap.getWidth(), bitmap.getHeight()};
            region = TexturePacker.get().addBitmap(bitmap);
        }
    }

    public void draw(DrawArg arg, SpriteBatch batch){
        batch.setColor(arg.color);
        batch.draw(region, (float) (arg.pos[0] - origin[0]),
                (float) (arg.pos[1] - origin[1] ),
                origin[0], origin[1],
                arg.stretch[0] != 0 ? arg.stretch[0] : dimensions[0],
                arg.stretch[1] != 0 ? arg.stretch[1] : dimensions[1],
                arg.xscale, arg.yscale, arg.angle);
    }
}
