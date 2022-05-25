package cn.treeh.Graphics.Text;

import cn.treeh.Util.Configure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.sun.tools.javac.util.StringUtils;

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
    Vector2 position;
    BackGround backGround;
    int maxWidth;
    int maxHeight;
    int height;
    String text;
    Font font;
    char[] textArray;
    LinkedList<Page> pages = new LinkedList<>();

    public Text(String text){
        this(text,Font.A11M, 800);
    }
    public Text(String text, Font font, int maxWidth, int maxHeight){
        this.text = text;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.font = font;
        pages = new LinkedList<>();
    }
    public Text(String text, Font font, int maxWidth){
        this(text, font, maxWidth, Integer.MAX_VALUE);
    }
    public void changeText(String text){
        this.text = text;
    }
    Page nowPage;
    Line nowLine;
    int nowHeight = 0;
    int nowWidth = 0;
    int nowPageHeight = 0;
    void addLine(){
        if(nowPageHeight + nowHeight > maxHeight)
            newPage();
        newLine();
    }
    void newLine(){
        nowPage.addLine(nowLine);
        nowLine = new Line();
        nowHeight = nowWidth = 0;
    }
    void newPage(){
        pages.add(nowPage);
        nowPage = new Page();
        nowPageHeight = 0;
    }
    //增加同样样式的文字
    void addWords(int first, int last){
        BitmapFont bitmapFont;
        Color color = Color.BLACK;
        switch (textArray[first]){
            case '\\':
                if(first + 1 < last){
                    switch (textArray[first + 1]){
                        case 'n':
                            addLine();
                            break;
                        case 'r':
                            if(nowWidth != 0){
                                addLine();
                                break;
                            }
                    }
                    first++;
                }
                first++;
                break;
            case '#':
                if(first + 1 < last){
                    switch (textArray[first + 1]) {
                        case 'k':
                            color = Color.DARK_GRAY;
                            break;
                        case 'b':
                            color = Color.BLUE;
                            break;
                        case 'r':
                            color = Color.RED;
                            break;
                        case 'c':
                            color = Color.ORANGE;
                    }
                    first++;
                }
                first++;
                break;
        }
        Words words = new Words();
        words.color = color;
        words.font = fontMap.get(font);
        BitmapFont.BitmapFontData fontData = words.font.getData();
        int charWidth, preIndex = first;
        BitmapFont.Glyph glyph;
        for(int index = first; index < last; index++){
            char c = textArray[index];
            glyph = fontData.getGlyph(c);
            charWidth = glyph.width;
            //超出一行了，先插入这一些words
            if(charWidth + nowWidth > maxWidth){
                words.start = preIndex;
                words.end = index;
                preIndex = index;
                nowLine.addWords(words);
                addLine();
                words = new Words();
                words.color = color;
                words.font = fontMap.get(font);
            }
            nowHeight = Math.max(nowHeight, glyph.height);
            nowWidth += charWidth;
        }
        if(nowWidth != 0 && last - preIndex >= 1) {
            words.start = preIndex;
            words.end = last;
            nowLine.words.add(words);
        }
    }
    public void reLayout(){
        nowPage = new Page();
        nowLine = new Line();
        textArray = text.toCharArray();
        int first = 0, length = text.length(), last = 0;
        while(first < length){
            last = StringUtils.indexOfIgnoreCase(text, "[\\#]", first + 1);
            if(last == -1)
                last = length;
            addWords(first, last);
            first = last;
        }

        //允许回收内存
        textArray = null;

    }
    public Vector2 draw(Vector2 pos, Batch batch){
        return draw(pos, batch, false);
    }
    public Vector2 draw(Vector2 pos, Batch batch, boolean underLine){
        return draw(pos, batch, 0, underLine);
    }
    public Vector2 draw(Vector2 pos, Batch batch, int page){
        return draw(pos, batch, page, false);
    }
    public Vector2 draw(Vector2 pos, Batch batch, int page, boolean underLine){
        return pages.get(page).draw(pos, batch, text, underLine);
    }

}
