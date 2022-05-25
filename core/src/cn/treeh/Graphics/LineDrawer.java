package cn.treeh.Graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

public class LineDrawer {
    static Texture pixel;
    static {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.BLACK);
        pixmap.drawPixel(0,0);
        pixel = new Texture(pixmap);
        pixmap.dispose();
    }
    public static void draw(Batch batch, Vector2 start, Vector2 end, float width){
        draw(batch, start.x, start.y, end.x, end.y, width, Color.BLACK);
    }
    public static void draw(Batch batch, Vector2 start, Vector2 end, float width, Color color){
        draw(batch, start.x, start.y, end.x, end.y, width, color);
    }
    public static void draw(Batch batch, float x1, float y1, float x2, float y2, float width) {
        draw(batch, x1, y1, x2, y2, width, Color.BLACK);
    }
    public static void draw(Batch batch, float x1, float y1, float x2, float y2, float width, Color color) {
        width = width / 2;
        float c = color.toFloatBits();
        batch.draw(pixel,
                new float[]{x1 + width, y1 - width, c, width, 0,
                        x1 - width, y1 + width, c, width, 0,
                        x2 - width, y2 + width, c, 0, 0,
                        x2 + width, y2 - width, c, 0, 0},
                0, 20);

    }
    public static void draw(Batch batch, int x1, int y1, int x2, int y2, int width){
        draw(batch, (float) x1, (float) y1, (float) x2, (float) y2, (float) width, Color.BLACK);
    }
    public static void draw(Batch batch, int x1, int y1, int x2, int y2, int width, Color color){
        draw(batch, (float) x1, (float) y1, (float) x2, (float) y2, (float) width, color);
    }
}
