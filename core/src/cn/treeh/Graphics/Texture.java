package cn.treeh.Graphics;

import cn.treeh.NX.Bitmap;
import cn.treeh.NX.Node;

public class Texture {
    Bitmap bitmap;
    int[] origin;
    public int[] dimensions;
    public Texture(Node src){
        if(src.getType() == Node.Type.bitmap){
            String link = src.subNode("source").getString("");
            if(!link.equals("") && !src.isRoot()){
                src = src.getRoot().subNode(link.substring(link.indexOf('/') + 1));
            }
            bitmap = src.getBitmap();
            origin = src.subNode("origin").getVector();
            dimensions = new int[]{bitmap.getWidth(), bitmap.getHeight()};
            GraphicsGL.get().addBitmap(bitmap);
        }
    }

    public void draw(DrawArg arg){
        GraphicsGL.get().add(bitmap, arg, origin[0], origin[1], dimensions[0], dimensions[1]);
    }
}
