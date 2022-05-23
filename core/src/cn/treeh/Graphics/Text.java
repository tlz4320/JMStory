package cn.treeh.Graphics;

import cn.treeh.Util.Configure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Text {
    public static HashMap<Font, BitmapFont> fontMap;
    static {//初始化字体
        fontMap = new HashMap<>();
        FreeTypeFontGenerator.setMaxTextureSize(10000);
        FreeTypeFontGenerator normFont = new FreeTypeFontGenerator(Gdx.files.internal(Configure.FONT_NORMAL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = Gdx.files.internal("charset.txt").readString("UTF8");
        parameter.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.size = 11;
        BitmapFont bf = normFont.generateFont(parameter);
        fontMap.put(Font.A11M, normFont.generateFont(parameter));
        parameter.size = 12;
        fontMap.put(Font.A12M, normFont.generateFont(parameter));
        parameter.size = 13;
        fontMap.put(Font.A13M, normFont.generateFont(parameter));
        parameter.size = 18;
        fontMap.put(Font.A18M, normFont.generateFont(parameter));
        normFont = new FreeTypeFontGenerator(Gdx.files.internal(Configure.FONT_BOLD));
        parameter.size = 11;
        fontMap.put(Font.A11B, normFont.generateFont(parameter));
        parameter.size = 12;
        fontMap.put(Font.A12B, normFont.generateFont(parameter));
        parameter.size = 13;
        fontMap.put(Font.A13B, normFont.generateFont(parameter));
        normFont = new FreeTypeFontGenerator(Gdx.files.internal(Configure.FONT_LIGHT));
        parameter.size = 11;
        fontMap.put(Font.A11L, normFont.generateFont(parameter));
    }
    public static enum Font{
        A11L(110),
        A11M(111),
        A11B(112),
        A12M(120),
        A12B(121),
        A13M(122),
        A13B(130),
        A18M(180);
        int value;

        Font(int i) {
            value = i;
        }
    }
    public enum Alignment{
        LEFT,
        CENTER,
        RIGHT
    }
    public enum BackGround{
        NONE,
        NAME
    }
    //merge same style words to one string make memory less usage
    static class Words{
        int start, end;
        Font font;
        Color color;
    }
    //line
    static class Line{
        Line(){
            words = new LinkedList<>();
        }
        LinkedList<Words> words;
    }
    Point position;
    int alignment;
    BackGround backGround;
    int maxWidth;
    int height;
    String text;
    LinkedList<Integer> advance = new LinkedList<>();
    LinkedList<Line> lines = new LinkedList<>();
    public Text(String text){
        this(text, 800);
    }
    public Text(String text, int maxWidth){
        this.text = text;
        this.maxWidth = maxWidth;
        lines = new LinkedList<>();
    }
    public void reLayout(){

    }
    public void draw(Point pos, Batch batch){
        BitmapFont bitmapFont;
        GlyphLayout l;
        float x, y = pos.y;
        for(Line line : lines){
            x = pos.x;
            for(Words words : line.words){
                bitmapFont = fontMap.get(words.font);
                bitmapFont.setColor(words.color);
                l = bitmapFont.draw(batch, text.substring(words.start, words.end), x, y);
                x += l.width;
            }
            y += height;
        }
    }

}
