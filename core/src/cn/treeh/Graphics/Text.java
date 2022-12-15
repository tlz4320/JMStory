package cn.treeh.Graphics;

import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.Configure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.github.tommyettinger.textra.Font;
import org.apache.commons.lang3.StringUtils;

public class Text {
    public static Font texraFont;
    public static BitmapFont bitmapFont;
    static {//初始化字体
        texraFont = new Font(Configure.FONT_NORMAL);
        FreeTypeFontGenerator.setMaxTextureSize(10000);
        FreeTypeFontGenerator normFont = new FreeTypeFontGenerator(Gdx.files.internal(Configure.FONT_LIGHT));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.characters = Gdx.files.internal("charset.txt").readString("UTF8");
        parameter.minFilter = com.badlogic.gdx.graphics.Texture.TextureFilter.Linear;
        parameter.magFilter = Texture.TextureFilter.Linear;
        parameter.size = 14;
        bitmapFont = normFont.generateFont(parameter);
    }

    String text;
    char[] textArray;
    public Text(String text){
        changeText(text);
    }
    public void changeText(String text){
        this.text = text;
    }


    StringBuilder processed = null;
    void addWords(String word){
        processed.append(word);
    }
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
                addWords(text.substring(first, first + 2));
                first += 2;
                break;
            case '#':
                if(first + 1 < last){
                    switch (textArray[first + 1]) {
                        case 'k':
//                            nowColor = Color.DARK_GRAY;
                            break;
                        case 'b':
//                            nowColor = Color.BLUE;
                            break;
                        case 'r':
//                            nowColor = Color.RED;
                            break;
                        case 'c':
                            // TODO: Show the number of items
//                            nowColor = Color.ORANGE;
                            break;
                        // #d - Purple text
                        case 'd':
//                            nowColor = Color.PURPLE;
                            break;
                        case 'g':
//                            nowColor = Color.GREEN;
                            break;

                        // #f[path]# - Shows an image within the WZ file
                        case 'f':
                        {
//                            Words words = new Words();
//                            byte[] bitmap = NXFiles.Item().subNode(text.substring(first + 2, last)).getBitmap().data();
//                            words.texture = new Texture(new Pixmap(bitmap, 0, bitmap.length));
//                            if(words.texture.getWidth() + nowWidth > maxWidth)
//                                addLine();
//                            nowLine.addWords(words);
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
//                            Words words = new Words();
                            first = text.indexOf("#l", last + 1);
                            //skip #
//                            words.selection = new Selection(text.substring(last + 1, first), font, maxWidth, id);
//                            if(words.selection.text.getHeight() + nowWidth > maxWidth)
//                                addLine();
//                            nowLine.addWords(words);
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
//                            switch (font) {
//                                case A11M:
//                                    nowFont = Font.A11B;
//                                    break;
//                                case A12M:
//                                    nowFont = Font.A12B;
//                                    break;
//                                case A13M:
//                                    nowFont = Font.A13B;
//                                    break;
//                                case A18M:
//                                    nowFont = Font.A18B;
//                                    break;
//                                default:
//                                    break;
//                            }
                            break;
                        }
                        case 'n':
                        case 'N':
                        {
//                            switch (font)
//                            {
//                                case A11B:
//                                    nowFont = Font.A11M;
//                                    break;
//                                case A12B:
//                                    nowFont = Font.A12M;
//                                    break;
//                                case A13B:
//                                    nowFont = Font.A13M;
//                                    break;
//                                case A18B:
//                                    nowFont = Font.A18M;
//                                    break;
//                            }
                            break;
                        }
                    }
                    first++;
                }
                first++;
                break;
            case ' ':
                first++;
                addWords("[][%50]");

        }
        addWords(text.substring(first, last));
        return last;
    }
    public void reLayout(){
        textArray = text.toCharArray();
        processed = new StringBuilder();
        int first = 0, length = text.length(), last = 0;
        while(first < length){
            last = StringUtils.indexOfIgnoreCase(text, "[ \\#\t]", first + 1);
            if(last == -1)
                last = length;
            first = addWords(first, last);
        }
        //允许回收内存
        textArray = null;
    }
    public void draw(int[] pos, float alpha){

    }
}
