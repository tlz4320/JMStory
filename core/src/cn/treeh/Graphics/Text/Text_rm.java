package cn.treeh.Graphics.Text;

import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.Configure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.math.Vector2;
import com.sun.tools.javac.util.StringUtils;

import java.util.HashMap;
import java.util.LinkedList;

public class Text_rm {
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
        parameter.size = 18;
        fontMap.put(Font.A18B, normFont.generateFont(parameter));
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
        A18M(180),
        A18B(181);
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

    public Text_rm(String text){
        this(text,Font.A11M, 800);
    }
    public Text_rm(String text, Font font, int maxWidth, int maxHeight){
        this.text = text;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.font = font;
        pages = new LinkedList<>();
    }
    public Text_rm(String text, Font font, int maxWidth){
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
    int maxPageHeight = 0;
    Font nowFont;
    Color nowColor;
    public int getHeight(){
        return maxPageHeight;
    }
    void addLine(){
        if(nowPageHeight + nowHeight > maxHeight)
            newPage();
        newLine();
        nowPageHeight += nowHeight;
    }
    void newLine(){
        nowPage.addLine(nowLine);
        nowLine = new Line();
        nowHeight = nowWidth = 0;
    }
    void newPage(){
        maxPageHeight = Math.max(maxPageHeight, nowPageHeight);
        pages.add(nowPage);
        nowPage = new Page();
        nowPageHeight = 0;
    }




    /**
     * 用于处理id转名字或者名字转id的情况
     * 这种情况下字符串和text是不一样的 因此单独处理
     * @param word
     */
    void addWords(String word){
        Words words = new Words();
        words.color = nowColor;
        words.font = fontMap.get(nowFont);
        BitmapFont.BitmapFontData fontData = words.font.getData();
        int charWidth, preIndex = 0;
        BitmapFont.Glyph glyph;
        char[] wordArray = word.toCharArray();
        for(int index = 0; index < wordArray.length; index++){
            char c = wordArray[index];
            glyph = fontData.getGlyph(c);
            charWidth = glyph.width;
            //超出一行了，先插入这一些words
            if(charWidth + nowWidth > maxWidth){
                words.word = word.substring(preIndex, index);
                preIndex = index;
                //这里就没有什么index了 所以直接用String  会稍微浪费内存  不过理论上也就在和npc对话对调用Text
                //就算全都存成字符串 也就顶多几K  之后对话结束直接回收好了
                nowLine.addWords(words);
                addLine();
                words = new Words();
                words.color = nowColor;
                words.font = fontMap.get(nowFont);
            }
            nowHeight = Math.max(nowHeight, glyph.height);
            nowWidth += charWidth;
        }
        if(nowWidth != 0 && wordArray.length - preIndex >= 1) {
            if(preIndex != 0)
                word = word.substring(preIndex);
            words.word = word;
            nowLine.words.add(words);
        }
    }
    //TODO 一个字符串是可以允许多种修饰的
    /**
     * 处理各种字符串操作
     * @param first first char position
     * @param last last char position + 1
     * @return last position used
     */
    int addWords(int first, int last){
        Color color = Color.BLACK;
        switch (textArray[first]){
            case '\\':
                if(first + 1 < last){
                    switch (textArray[first + 1]){
                        case 'n':
                            if(nowWidth != 0)
                                addLine();
//                            nowColor = color;
//                            nowFont = font;
                            break;
                        case 'r':
                            if(nowWidth != 0){
                                addLine();
                            }
                            break;
                    }
                    first++;
                }
                first++;
                break;
            case '#':
                if(first + 1 < last){
                    switch (textArray[first + 1]) {
                        case 'k':
                            nowColor = Color.DARK_GRAY;
                            break;
                        case 'b':
                            nowColor = Color.BLUE;
                            break;
                        case 'r':
                            nowColor = Color.RED;
                            break;
                        case 'c':
                            // TODO: Show the number of items
                            nowColor = Color.ORANGE;
                            break;
                        // #d - Purple text
                        case 'd':
                            nowColor = Color.PURPLE;
                            break;
                        case 'g':
                            nowColor = Color.GREEN;
                            break;

                        // #f[path]# - Shows an image within the WZ file
                        case 'f':
                        {
                            Words words = new Words();
                            byte[] bitmap = NXFiles.Item().subNode(text.substring(first + 2, last)).getBitmap().data();
                            words.texture = new Texture(new Pixmap(bitmap, 0, bitmap.length));
                            if(words.texture.getWidth() + nowWidth > maxWidth)
                                addLine();
                            nowLine.addWords(words);
                            return last;
                        }
                        // #i[id]# - Shows a picture of the given item
                        case 'i':
                        {
                            // TODO: Needs implemented
                            return last;
                        }
                        case 'l':
                        {
                            //should process by L
                            return first + 2;
                        }
                        // #L[number]# - Starts a selection for the number of items given
                        case 'L':
                        {
                            //skip #L
                            int id = Integer.parseInt(text.substring(first + 2, last));
                            Words words = new Words();
                            first = text.indexOf("#l", last + 1);
                            //skip #
                            words.selection = new Selection(text.substring(last + 1, first), font, maxWidth, id);
                            if(words.selection.text.getHeight() + nowWidth > maxWidth)
                                addLine();
                            nowLine.addWords(words);
                            //TODO: should return the length left but not void
                            return first;
                        }
                        // #s[id]# - Shows the image of the given skill
                        case 's':
                        {
                            // TODO: Needs implemented
                            return last;
                        }
                        // #v[id]# - Shows a picture of the given item
                        case 'v': {
                            int id = Integer.parseInt(text.substring(first + 1, last));

                            return last;
                        }

                        // TODO: Is there a space between the h and ending # or not?
                        // #h # - Shows the name of the player
                        case 'h':
                        {

                            return last + 1;
                        }
                        // #o[id]# - Shows the name of the given monster
                        case 'o':
                        {
                            int id = Integer.parseInt(text.substring(first + 2, last));
                            String tmpS = NXFiles.String().subNode("Mob.img").subNode(id).subNode("name").getString("");
                            addWords(tmpS);
                            return last + 1;
                        }
                        // #p[id]# - Shows the name of the given NPC
                        case 'p':
                        {
                            // TODO: Needs implemented
                            int id = Integer.parseInt(text.substring(first + 2, last));
                            String tmpS = NXFiles.String().subNode("Npc.img").subNode(id).subNode("name").getString("");
                            addWords(tmpS);
                            return last + 1;
                        }
                        // #q[id]# - Shows the name of the given skill
                        case 'q':
                        {
                            int id = Integer.parseInt(text.substring(first + 2, last));
                            String tmpS = NXFiles.String().subNode("Skill.img").subNode(id).subNode("bookName").getString("");
                            addWords(tmpS);
                            return last + 1;
                        }

                        // Shows the name of the given item
                        case 't':
                        case 'z': {
                            int id = Integer.parseInt(text.substring(first + 2, last));
                            int type = id / 1000000;
                            Node node = NXFiles.String().subNode("Item.img");
                            String tmpS = "";
                            if (id == 1112223 || id == 1112224 || id == 1112225 || type == 5) {
                                if (id < 5000100)
                                    tmpS = node.subNode("Pet").subNode(id).subNode("name").getString("");
                                else
                                    tmpS = node.subNode("Cash").subNode(id).subNode("name").getString("");
                            } else if (type == 2)
                                tmpS = node.subNode("Con").subNode(id).subNode("name").getString("");
                            else if (type <= 1) {
                                node = NXFiles.String().subNode("Eqp.img").subNode("Eqp");
                                if (id > 1000000 && id < 1003060)
                                    tmpS = node.subNode("Cap").subNode(id).subNode("name").getString("");
                                else if (id >= 1102000 && id <= 1102233)
                                    tmpS = node.subNode("Cape").subNode(id).subNode("name").getString("");
                                else if (id >= 1040000 && id <= 1049000)
                                    tmpS = node.subNode("Coat").subNode(id).subNode("name").getString("");
                                else if (id >= 20000 && id <= 21819)
                                    tmpS = node.subNode("Face").subNode(id).subNode("name").getString("");
                                else if (id >= 1080000 && id <= 1082260)
                                    tmpS = node.subNode("Glove").subNode(id).subNode("name").getString("");
                                else if (id >= 30000 && id <= 39999)
                                    tmpS = node.subNode("Hair").subNode(id).subNode("name").getString("");
                                else if (id >= 1050000 && id <= 1059999)
                                    tmpS = node.subNode("Longcoat").subNode(id).subNode("name").getString("");
                                else if (id >= 1060000 && id <= 1069999)
                                    tmpS = node.subNode("Pants").subNode(id).subNode("name").getString("");
                                else if (id >= 1802000 && id <= 1812001)
                                    tmpS = node.subNode("PetEquip").subNode(id).subNode("name").getString("");
                                else if (id >= 1112000 && id <= 1122999)
                                    tmpS = node.subNode("Ring").subNode(id).subNode("name").getString("");
                                else if (id >= 1090000 && id <= 1099999) {
                                    if(id == 1092061)
                                        tmpS = node.subNode("Weapon").subNode(id).subNode("name").getString("");
                                    else
                                        tmpS = node.subNode("Shield").subNode(id).subNode("name").getString("");
                                }
                                else if (id >= 1070000 && id <= 1079999)
                                    tmpS = node.subNode("Shoes").subNode(id).subNode("name").getString("");
                                else if (id >= 1902000 && id <= 1912038)
                                    tmpS = node.subNode("Taming").subNode(id).subNode("name").getString("");
                                else if (id >= 1300000 && id <= 1799999)
                                    tmpS = node.subNode("Weapon").subNode(id).subNode("name").getString("");
                                else
                                    tmpS = node.subNode("Accessory").subNode(id).subNode("name").getString("");
                            } else if (type == 4)
                                tmpS = node.subNode("Etc").subNode(id).subNode("name").getString("");
                            else if (type == 3)
                                tmpS = node.subNode("Ins").subNode(id).subNode("name").getString("");
                            addWords(tmpS);
                            return last + 1;
                        }
                        // #m[id]# - Shows the name of the given map
                        case 'm':
                        {
                            int id = Integer.parseInt(text.substring(first + 2, last));
                            String tmpS = NXFiles.String().subNode("Map.img").subNode("chinese").
                                    subNode(id).subNode("mapName").getString("");
                            addWords(tmpS);
                            return last + 1;
                        }

                        case 'x':
                        {
                            return text.length();
                        }
                        // #B[%]# - Shows a progress bar
                        case 'B':
                        {
                            // TODO: Needs implemented
                            break;
                        }
                        case 'e':
                        case 'E': {
                            switch (font) {
                                case A11M:
                                    nowFont = Font.A11B;
                                    break;
                                case A12M:
                                    nowFont = Font.A12B;
                                    break;
                                case A13M:
                                    nowFont = Font.A13B;
                                    break;
                                case A18M:
                                    nowFont = Font.A18B;
                                    break;
                                default:
                                    break;
                            }
                            break;
                        }
                        case 'n':
                        case 'N':
                        {
                            switch (font)
                            {
                                case A11B:
                                    nowFont = Font.A11M;
                                    break;
                                case A12B:
                                    nowFont = Font.A12M;
                                    break;
                                case A13B:
                                    nowFont = Font.A13M;
                                    break;
                                case A18B:
                                    nowFont = Font.A18M;
                                    break;
                            }
                            break;
                        }
                    }
                    first++;
                }
                first++;
                break;
            case ' ':
                first++;
                nowFont = font;
                nowColor = color;

        }
        Words words = new Words();
        words.color = nowColor;
        words.font = fontMap.get(nowFont);
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
                words.color = nowColor;
                words.font = fontMap.get(nowFont);
            }
            nowHeight = Math.max(nowHeight, glyph.height);
            nowWidth += charWidth;
        }
        if(nowWidth != 0 && last - preIndex >= 1) {
            words.start = preIndex;
            words.end = last;
            nowLine.words.add(words);
        }
        return last;
    }
    public void reLayout(){
        nowPage = new Page();
        nowLine = new Line();
        textArray = text.toCharArray();
        int first = 0, length = text.length(), last = 0;
        while(first < length){
            last = StringUtils.indexOfIgnoreCase(text, "[ \\#\t]", first + 1);
            if(last == -1)
                last = length;
            first = addWords(first, last);
        }
        if(nowWidth != 0)
            addLine();
        if(nowPageHeight != 0)
            newPage();
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
