package cn.treeh.Graphics.Text;

import cn.treeh.Graphics.LineDrawer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Vector2;

import java.awt.*;

public class Words {
    int start, end;
    BitmapFont font;
    Color color;
    Texture texture;
    Selection selection;
    String word;
    //同样 最后画完之后返回一个宽度和高度
    //这个text我在想是函数传入还是直接放words里面 理论上也不占内存
    public Vector2 draw(Vector2 pos, Batch batch, String text, boolean underLine){
        if(selection != null) {
            return selection.draw(pos, batch);
        }
        if(texture != null){
            batch.draw(texture, pos.x, pos.y);
            return new Vector2(texture.getWidth(), texture.getHeight());
        }
        font.setColor(color);
        if(word == null)
            word = text.substring(start, end);
        //因为Freetype的bitmap是从上到下画的 这个y要加上一个字体大小的偏移
        GlyphLayout l = font.draw(batch, word, pos.x, pos.y + font.getCapHeight());
        //加上当前已经占用的宽度
        if(underLine)//用于选择框高亮
            LineDrawer.draw(batch, pos.x, pos.y + font.getCapHeight(),
                    pos.x + l.width, pos.y + font.getCapHeight(), 1.0F, color);
        return new Vector2(l.width, l.height);
    }
}
