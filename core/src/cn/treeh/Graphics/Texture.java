package cn.treeh.Graphics;

import cn.treeh.NX.Bitmap;
import cn.treeh.NX.Node;

public class Texture {
    Bitmap bitmap;
    int[] origin;
    int[] dimensions;
    public Texture(Node src){
        if(src.getType() == Node.Type.bitmap){
            String link = src.subNode("source").getString("");
            if(!link.equals("") && !src.isRoot()){
                src = src.getRoot().subNode(link.substring(link.indexOf('/') + 1));
            }
            bitmap = src.getBitmap();
            origin = src.subNode("origin").getVector();
            dimensions = new int[]{bitmap.getWidth(), bitmap.getHeight()};
        }
    }
}
