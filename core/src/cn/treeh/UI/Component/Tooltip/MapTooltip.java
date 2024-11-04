package cn.treeh.UI.Component.Tooltip;

import cn.treeh.Graphics.LineDrawer;
import cn.treeh.Graphics.Text;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxUtils;
import cn.treeh.UI.Component.MapleFrame;
import cn.treeh.Util.Configure;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TextraLabel;

import java.util.HashMap;
import java.util.Map;

public class MapTooltip extends Tooltip{
    static int MAX_LIFE = 100;
    static int[] SEPARATOR_ADJ = new int[]{1, 10};
    static int[] LIFE_LABEL_ADJ = new int[]{16, 3};
    static int[] LIFE_ICON_ADJ = new int[]{1, 9};

//    MapleFrame frame;

//    Texture cover;
//    Texture Mob;
//    Texture Npc;

    //Texture Party;

    Tooltip.Parent parent = Parent.NONE;

    String title = "";
    String description = "";

    TextraLabel title_label;
    TextraLabel desc_label;
    TextraLabel[] mob_labels = new TextraLabel[MAX_LIFE];
    TextraLabel[] npc_labels = new TextraLabel[MAX_LIFE];

    int fillwidth = 0;
    int fillheight = 0;

    LineDrawer separator;
    public MapTooltip(){
//        Node frame = NXFiles.UI().subNode("UIToolTip.img/Item/Frame2");
//        Node WorldMap = NXFiles.UI().subNode("UIWindow.img/ToolTip/WorldMap");
//        this.frame = new MapleFrame(frame);
//        this.cover = new Texture(frame.subNode("cover"));
//        Mob = new Texture(WorldMap.subNode("Mob"));
//        Npc = new Texture(WorldMap.subNode("Npc"));
        //Party = new Texture(WorldMap.subNode("Party"));
    }

    public void draw(int[] position, SpriteBatch batch)
    {
        position = position.clone();
        if (title_label == null ||
                title_label.storedText == null ||
                title_label.storedText.length() == 0)
            return;

        int max_width = Configure.screenWidth;
        int max_height = Configure.screenHeight;
        int cur_width = position[0] + fillwidth + 14 + 9 + 17;
        int cur_height = position[1] + fillheight + 14 + 4 + 6;
        int adj_x = cur_width - max_width;
        int adj_y = cur_height - max_height;

        if (adj_x > 0)
            position[0] += adj_x * -1;

        if (adj_y > 0)
            position[1] += adj_y * -1;

        // Shift everything so the cursor is in the top left corner
        position[0] += 20;
        position[1] -= 3;

        switch (parent)
        {
            case WORLDMAP:
                drawWorldMap(position, batch);
                break;
            case MINIMAP:
                drawMinimap(position, batch);
                break;
            default:
                break;
        }
    }

    public void setTitle(Tooltip.Parent p, String t, boolean b){
        if(parent == p && title.equals(t))
            return;
        parent = p;
        title = t;
        if(title.length() == 0)
            return;
        switch (parent){
            case WORLDMAP:
                set_worldmap_title(b);
                break;
            case MINIMAP:
                set_minimap_title(b);
                break;
            default:
                break;
        }

    }
    @Override
    public void draw(int x, int y) {

    }
    void setDesc(String d){
        if(description.equals(d))
            return;
        description = d;
        if(description.length() == 0)
            return;
        switch (parent){
            case WORLDMAP:
            set_worldmap_desc();
                break;
            case MINIMAP:
            set_minimap_desc();
                break;
            default:
                break;
        }
    }
    void setMapId(int mapid, boolean portal){
        HashMap<Integer, Map.Entry<String, String>> life = NxUtils.getMapLife(mapid);
        switch (parent)
        {
            case WORLDMAP:
            set_worldmap_mapid(life, portal);
                break;
            case MINIMAP:
            set_minimap_mapid(life, portal);
                break;
            default:
                break;
        }
    }
    public void reset() {
        parent = Parent.NONE;

        title = "";
        title_label.setText("");

        description = "";
        desc_label.setText("");

        for (int i = 0; i < MAX_LIFE; i++) {
            if (mob_labels[i] != null)
                mob_labels[i].setText("");
            if (npc_labels[i] != null)
                npc_labels[i].setText("");
        }

        fillwidth = 0;
        fillheight = 0;
    }
    public void drawWorldMap(int[] pos, SpriteBatch batch) {
        int width = fillwidth - 8;
        int halfwidth = width / 2;
        pos[0] += 9;
        pos[1] += 4;
        title_label.setPosition(pos[0], pos[1]);
        title_label.draw(batch, 1);
        if (desc_label.storedText != null) {
            pos[1] += title_label.getHeight() + 5;
            desc_label.setPosition(pos[0], pos[1]);
            desc_label.draw(batch, 1);
            pos[1] += desc_label.getHeight();
        } else {
            pos[1] += title_label.getHeight() - 2;
        }
//        for(int i = 0; i < MAX_LIFE; i++){
//            TextraLabel mob = mob_labels[i];
//            if(mob != null && mob.storedText != null && mob.storedText.length() != 0){
//                LineDrawer.draw(batch, );
//            }
//        }
        pos[1] += 10;
        for (int i = 0; i < MAX_LIFE; i++) {
            TextraLabel npc = npc_labels[i];
            if (npc != null && npc.storedText != null && npc.storedText.length() != 0) {
                {
                    npc.setPosition(pos[0] + LIFE_LABEL_ADJ[0], pos[1] + LIFE_LABEL_ADJ[1]);
                    npc.draw(batch, 1);
                    pos[1] += npc.getHeight() + 1;
                }
            }
        }
    }
    void drawMinimap(int[] position, SpriteBatch batch)
    {
        boolean desc_empty = desc_label == null || desc_label.storedText == null || desc_label.storedText.length() == 0;
//
//        if (desc_empty && mob_labels->empty() && npc_labels->empty())
//        {
//            int16_t width = fillwidth - 12;
//            int16_t halfwidth = width / 2;
//
//            int16_t height = fillheight - title_label.height();
//
//            frame.draw(position + Point<int16_t>(halfwidth, height) + Point<int16_t>(14, 14), width, height);
//
//            position.shift(Point<int16_t>(9, 2));
//
//            title_label.draw(position + Point<int16_t>(halfwidth + 5, 0));
//        }
//        else
//        {
//            int16_t width = fillwidth - (!desc_empty ? 12 : 8);
//            int16_t halfwidth = width / 2;
//
//            int16_t height = fillheight - 2;
//
//            frame.draw(position + Point<int16_t>(halfwidth, height) + Point<int16_t>(14, 14), width, height);
//            cover.draw(position + Point<int16_t>(0, 1));
//
//            if (!desc_empty)
//                position.shift(Point<int16_t>(7, 2));
//			else
//            position.shift(Point<int16_t>(9, 4));
//
//            title_label.draw(position + Point<int16_t>(halfwidth + 7, 0));
//            position.shift_y(title_label.height());
//
//            if (!desc_empty)
//            {
//                separator.draw(position + SEPARATOR_ADJ - Point<int16_t>(0, 8));
//
//                desc_label.draw(position + Point<int16_t>(halfwidth + 7, 0));
//                position.shift_y(desc_label.height());
//            }
//
//            for (uint8_t i = 0; i < MAX_LIFE; i++)
//            {
//                Text mob = mob_labels[i];
//
//                if (!mob.empty())
//                {
//                    if (i == 0) {
//                        separator.draw(position + SEPARATOR_ADJ - Point<int16_t>(0, 2));
//                        position.shift_y(8);
//
//                        Mob.draw(position + LIFE_ICON_ADJ);
//                    }
//
//                    mob.draw(position + LIFE_LABEL_ADJ);
//                    position.shift_y(mob.height() + 1);
//                }
//            }
//
//            for (uint8_t i = 0; i < MAX_LIFE; i++)
//            {
//                Text npc = npc_labels[i];
//
//                if (!npc.empty())
//                {
//                    if (i == 0) {
//                        separator.draw(position + SEPARATOR_ADJ - Point<int16_t>(0, 2));
//                        position.shift_y(8);
//
//                        Npc.draw(position + LIFE_ICON_ADJ);
//                    }
//
//                    npc.draw(position + LIFE_LABEL_ADJ);
//                    position.shift_y(npc.height() + 1);
//                }
//            }
//        }
    }
    void set_worldmap_title(boolean bolded)
    {
        fillwidth = 206;
        title_label = new TextraLabel(title, Text.texraFont, Color.WHITE);
        title_label.setAlignment(Align.center);
        float width = title_label.getWidth();
        float height = title_label.getHeight();

        if (width > fillwidth)
            fillwidth = (int)width;
        if (height > fillheight)
            fillheight = (int)height;
    }

    void set_minimap_title(boolean bolded)
    {
        title_label = new TextraLabel(title, Text.texraFont, Color.WHITE);
        title_label.setAlignment(Align.center);
        float width = title_label.getWidth();
        float height = title_label.getHeight();

        if (width > fillwidth)
            fillwidth = (int)width;
        if (height > fillheight)
            fillheight = (int)height;
    }
    void set_worldmap_desc()
    {
        desc_label =new TextraLabel(description, Text.texraFont, Color.WHITE);
        desc_label.setAlignment(Align.center);
        float width = desc_label.getWidth();
        float height = desc_label.getHeight();

        if (width > fillwidth)
            fillwidth = (int)width;
        if (height > fillheight)
            fillheight = (int)height;
    }

    void set_minimap_desc()
    {
        desc_label =new TextraLabel(description, Text.texraFont, Color.WHITE);
        desc_label.setAlignment(Align.center);
        float width = desc_label.getWidth();
        float height = desc_label.getHeight();

        if (width > fillwidth)
            fillwidth = (int)width;
        if (height > fillheight)
            fillheight = (int)height;
    }
    void set_worldmap_mapid(HashMap<Integer, Map.Entry<String, String>> life, boolean portal)
    {
        int m = 0;
        int n = 0;
        boolean desc_empty = desc_label == null || desc_label.storedText == null;

        if (!desc_empty)
            fillheight += title_label.getHeight() + 7;

        for (Map.Entry<Integer, Map.Entry<String, String>> l : life.entrySet())
        {
            Map.Entry<String, String> life_object = l.getValue();

            if (life_object.getKey().equals("m") && m < MAX_LIFE)
            {
                mob_labels[m] = new TextraLabel(life_object.getValue(), Text.texraFont, Color.CHARTREUSE);
                fillheight += mob_labels[m].getHeight() + 1;
                m++;
            }
            else if (life_object.getKey().equals("n") && n < MAX_LIFE)
            {
                npc_labels[n] = new TextraLabel(life_object.getValue(), Text.texraFont,
                        new Color(0.47f, 0.8f, 1.0f, 1));

                if (m > 0 && n == 0)
                    fillheight += 8;

                fillheight += npc_labels[n].getHeight() + 1;

                n++;
            }
        }

        fillheight -= 3;

        if (desc_empty && m == 0 && n == 0)
            fillheight -= 8;
    }

    void set_minimap_mapid(HashMap<Integer, Map.Entry<String, String>> life, boolean portal)
    {
        int m = 0;
        int n = 0;
        boolean desc_empty = desc_label == null || desc_label.storedText == null;

        if (!desc_empty)
            fillheight += title_label.getHeight() + 7;

        for (Map.Entry<Integer, Map.Entry<String, String>> l : life.entrySet())
        {
            Map.Entry<String, String> life_object = l.getValue();


            if (life_object.getKey().equals("m") && m < MAX_LIFE)
            {
                mob_labels[m] = new TextraLabel(life_object.getValue(), Text.texraFont, Color.CHARTREUSE);

                int width = (int)mob_labels[m].getWidth() + 8;

                if (width > fillwidth)
                    fillwidth = width;

                fillheight += mob_labels[m].getHeight() + 1;

                m++;
            }
            if (life_object.getKey().equals("n") && m < MAX_LIFE)
            {
                npc_labels[n] = new TextraLabel(life_object.getValue(), Text.texraFont,
                        new Color(0.47f, 0.8f, 1.0f, 1));
                int width = (int)npc_labels[n].getWidth() + 8;

                if (width > fillwidth)
                    fillwidth = width;


                if (m > 0 && n == 0)
                    fillheight += 8;

                fillheight += npc_labels[n].getHeight() + 1;

                n++;
            }
        }

        if (portal)
        {
            fillheight -= 1;

            if (desc_empty && m == 0 && n == 0)
                fillheight -= 8;
        }
    }
}