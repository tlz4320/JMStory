package cn.treeh.Graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class DrawObj{
    TextureRegion region;
    float x, y;
    int w,h;
    int orix, oriy;
    DrawArg arg;
    public DrawObj(TextureRegion region, DrawArg _arg,
                   int _orix, int _oriy, int _w, int _h){
        this.region = region;
        arg = _arg;
        x = _arg.pos[0];
        y = _arg.pos[1];
        w = _w;
        h = _h;
        if(arg.stretch != null){
            w = arg.stretch[0];
            h = arg.stretch[1];
        }
        orix = _orix;
        oriy = _oriy;
    }
    public void draw(SpriteBatch batch){
        batch.setColor(arg.color);
        batch.draw(region, x, y, orix, oriy, w, h, arg.xscale, arg.yscale, arg.angle);
    }
}
