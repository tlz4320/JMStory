package cn.treeh.Graphics;


import cn.treeh.NX.Bitmap;
import cn.treeh.Util.Configure;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.LinkedList;
import java.util.TreeMap;

public class GraphicsGL {

    LinkedList<DrawObj> drawObj;
    MPixmapPacker pixmapPacker;
//    Texture texture;
    TreeMap<Long, TextureRegion> offsets;
    static GraphicsGL graphicsGL;
    public static GraphicsGL get() {
        if (graphicsGL == null) {
            graphicsGL = new GraphicsGL();
//            graphicsGL.texture = new Texture(Configure.screenWidth, Configure.screenHeight, Pixmap.Format.RGBA8888);
            graphicsGL.offsets = new TreeMap<>();
            graphicsGL.drawObj = new LinkedList<>();
            graphicsGL.pixmapPacker = new MPixmapPacker(8192, 8192, Pixmap.Format.RGBA8888, 2, false);
        }
        return graphicsGL;
    }
    //
    public void add(Bitmap bitmap, DrawArg arg, int orix, int oriy, int width, int height){
        drawObj.add(new DrawObj(offsets.get(bitmap.getID()), arg, orix, oriy, width, height));
    }
    public void addBitmap(Bitmap bitmap){
        long id = bitmap.getID();
        if(offsets.containsKey(id))
            return;
        Pixmap pixmap = new Pixmap(bitmap.getWidth(), bitmap.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setPixels(bitmap.data());
        offsets.put(id, pixmapPacker.pack(pixmap));
    }
    //read draw function,
    public void draw(SpriteBatch batch){
        drawObj.forEach(obj -> obj.draw(batch));
        drawObj.clear();
    }
}
