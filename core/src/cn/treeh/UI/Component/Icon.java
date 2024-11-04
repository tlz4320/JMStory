package cn.treeh.UI.Component;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Icon {
    public enum IconType {
        NONE,
        SKILL,
        EQUIP,
        ITEM,
        KEY,
        NUM_TYPES
    }
    public
    IconType type;
    boolean show_count;
    int count;

    Texture texture;
    boolean dragged;
    int[] cursorOffset = new int[2];
    public Icon(){
        this(IconType.NONE, null, -1);
    }

    public Icon(IconType type, Texture t, int c)
    {
        this.type = type;
        this.texture = t;
        this.count = c;
        texture.shift(new int[]{0, 32});

        show_count = count > -1;
        dragged = false;
    }
    static Charset countset = new Charset(NXFiles.UI().subNode("Basic.img/ItemNo"), Charset.Alignment.LEFT);
    public void draw(int[] pos, SpriteBatch batch){
        float opacity = dragged ? 0.5f : 1.0f;
        DrawArg arg = new DrawArg(pos, opacity);
        if(texture != null)
            texture.draw(arg, batch);
        arg.setOpacity(1);
        arg.addPos(0, 20);
        if (show_count)
        {
            countset.draw("" + count, arg, batch);
        }
    }
    public void dragDraw(SpriteBatch batch)
    {
        DrawArg arg = new DrawArg(new int[]{Gdx.input.getX() - cursorOffset[0],
                Gdx.input.getY() - cursorOffset[1]}, 0.5f);
        if (dragged && texture != null)
            texture.draw(arg, batch);
    }

}
