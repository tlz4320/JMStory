package cn.treeh.Graphics;

import cn.treeh.NX.Bitmap;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;

public class TexturePacker {
    private TexturePacker(){}
    TreeMap<Long, TextureRegion> Regions;
    MPixmapPacker buffer;
    static TexturePacker packer;
    public static TexturePacker get() {
        if (packer == null) {
            packer = new TexturePacker();
            packer.Regions = new TreeMap<>();
            packer.buffer = new MPixmapPacker(8192, 8192, Pixmap.Format.RGBA8888, 2, false);
        }
        return packer;
    }
    //
    public TextureRegion addBitmap(Bitmap bitmap){
        long id = bitmap.getID();
        if(Regions.containsKey(id))
            return Regions.get(id);
        Pixmap pixmap = new Pixmap(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setPixels(bitmap.data());
        TextureRegion region = buffer.pack(pixmap);
        Regions.put(id, region);
        return region;
    }
}
