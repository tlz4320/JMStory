package cn.treeh.Graphics;

import cn.treeh.Util.Configure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeType;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class Text {
    static HashMap<Font, BitmapFont> fontMap;
    static {//初始化字体
        fontMap = new HashMap<>();
        FreeTypeFontGenerator normFont = new FreeTypeFontGenerator(Gdx.files.internal(Configure.FONT_NORMAL));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 11;
        fontMap.put(Font.A11L, normFont.generateFont(parameter));
        fontMap.put(Font.A11M, normFont.generateFont(parameter));
        parameter.size = 12;
        fontMap.put(Font.A12M, normFont.generateFont(parameter));
        parameter.size = 13;
        fontMap.put(Font.A13M, normFont.generateFont(parameter));
        parameter.size = 18;
        fontMap.put(Font.A18M, normFont.generateFont(parameter));
        fontMap = new HashMap<>();
        normFont = new FreeTypeFontGenerator(Gdx.files.internal(Configure.FONT_BOLD));
        parameter.size = 11;
        fontMap.put(Font.A11B, normFont.generateFont(parameter));
        parameter.size = 12;
        fontMap.put(Font.A12B, normFont.generateFont(parameter));
        parameter.size = 13;
        fontMap.put(Font.A13B, normFont.generateFont(parameter));
    }
    public static enum Font{
        A11L,
        A11M,
        A11B,
        A12M,
        A12B,
        A13M,
        A13B,
        A18M
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
        String str;
        Font font;
        Color color;
    }
    //line
    static class Line{
        LinkedList<Words> words;
    }
    Point position;
    Alignment alignment;
    BackGround backGround;
    int maxWidth;
    int width;
    int height;
    String text;
    LinkedList<Integer> advance = new LinkedList<>();
    public Text(String text){
        this(text, 800);
    }
    public Text(String text, int maxWidth){
        this.text = text;
        this.maxWidth = maxWidth;
    }
    public void reLayout(){

    }
    public void draw(Point pos){
    }

}
