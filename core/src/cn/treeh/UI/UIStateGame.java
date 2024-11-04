package cn.treeh.UI;

import cn.treeh.Audio.SoundPlayer;
import cn.treeh.Game.GamePlay;
import cn.treeh.Game.Player.CharStat;
import cn.treeh.IO.Key.KeyType;
import cn.treeh.UI.Component.Icon;
import cn.treeh.UI.Component.Tooltip.*;
import cn.treeh.UI.Elements.UIElement;
import cn.treeh.UI.Elements.UIStatusBar;
import cn.treeh.Util.Configure;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.*;

public class UIStateGame implements UIState {
    LinkedHashMap<UIElement.Type, UIElement> elements = new LinkedHashMap<>();
    UIElement dragged;
    UIElement.Type focused;
    LinkedList<UIElement.Type> elementOrder = new LinkedList<>();
    CharStat stats;
    Tooltip tooltip;
    EquipTooltip equipTooltip;
    ItemTooltip itemTooltip;
    SkillTooltip skillTooltip;
    TextTooltip textTooltip;
    MapTooltip mapTooltip;
    Tooltip.Parent tooltipparent;
    Icon draggedIcon;
    Stage stage;
    SpriteBatch batch;
    int WIDTH = Configure.screenWidth, HEIGHT = Configure.screenHeight;
    static HashMap<Icon.IconType, UIElement.Type> icon_map = new HashMap<>();
    static {
        icon_map.put(Icon.IconType.NONE, UIElement.Type.NONE);
        icon_map.put(Icon.IconType.SKILL, UIElement.Type.SKILLBOOK);
        icon_map.put(Icon.IconType.EQUIP, UIElement.Type.EQUIPINVENTORY);
        icon_map.put(Icon.IconType.ITEM, UIElement.Type.ITEMINVENTORY);
        icon_map.put(Icon.IconType.KEY, UIElement.Type.KEYCONFIG);
        icon_map.put(Icon.IconType.NUM_TYPES, UIElement.Type.NUM_TYPES);
    }
    public UIStateGame(SpriteBatch b, Stage s){
        stats = GamePlay.getInstance().getPlayer().getStats();
        focused = UIElement.Type.NONE;
        batch = b;
        stage = s;
    }

    @Override
    public void draw(float inter, int[] cursor) {

    }

    @Override
    public void draw(float inter, SpriteBatch batch) {
        for(UIElement.Type type : elementOrder){
            UIElement element = elements.get(type);
            if(element != null && element.getActive()){
                element.draw(inter);
            }
        }
        if(tooltip != null){
            tooltip.draw();
        }
        if(draggedIcon != null)
            draggedIcon.dragDraw(batch);
    }

    @Override
    public void update() {
        boolean update_screen = false;
        int new_width = Configure.screenWidth;
        int new_height = Configure.screenHeight;

        if (WIDTH != new_width || HEIGHT != new_height)
        {
            update_screen = true;
            WIDTH = new_width;
            HEIGHT = new_height;

            UI.getInstance().remove(UIElement.Type.STATUSBAR);

			CharStat stats = GamePlay.getInstance().getPlayer().getStats();
            emplace(new UIStatusBar(batch, stage, stats));
        }
    }

    @Override
    public void send_key(KeyType type, int action, boolean pressed, boolean escape) {

    }

    @Override
    public void send_scroll(double yoffset) {

    }

    @Override
    public void send_close() {

    }

    @Override
    public void drag_icon(Icon icon) {

    }

    @Override
    public void clear_tooltip(Tooltip.Parent parent) {

    }

    @Override
    public void show_equip(Tooltip.Parent parent, int slot) {

    }

    @Override
    public void show_item(Tooltip.Parent parent, int itemid) {

    }

    @Override
    public void show_skill(Tooltip.Parent parent, int skill_id, int level, int masterlevel, int expiration) {

    }

    @Override
    public void show_text(Tooltip.Parent parent, String text) {

    }

    @Override
    public void show_map(Tooltip.Parent parent, String name, String description, int mapid, boolean bolded, boolean portal) {
//        matooltip.set_title(parent, title, bolded);
//        matooltip.set_desc(description);
//        matooltip.set_mapid(mapid, portal);
//
//        if (!title.empty())
//        {
//            tooltip = matooltip;
//            tooltipparent = parent;
//        }
    }

    @Override
    public void remove(UIElement.Type type) {
        if (type == focused)
            focused = UIElement.Type.NONE;

        if (type.ordinal() == tooltipparent.ordinal())
            clear_tooltip(tooltipparent);

        elementOrder.remove(type);

        if (elements.containsKey(type))
        {
            UIElement element = elements.get(type);
            element.deActive();
            elements.remove(type);
        }
    }

    @Override
    public UIElement get(UIElement.Type type) {
        return elements.get(type);
    }

    @Override
    public UIElement get_front(HashSet<UIElement.Type> types) {
        for(UIElement.Type type : elementOrder){
            if(types.contains(type) && elements.containsKey(type)){
                UIElement element = elements.get(type);
                if(element != null && element.getActive())
                    return element;
            }
        }
        return null;
    }

    @Override
    public UIElement get_front(int[] cursor_position) {
        for (UIElement.Type type : elementOrder) {

            UIElement element = elements.get(type);
            if (element != null && element.getActive() && element.inRange(cursor_position))
                return element;

        }
        return null;
    }

    @Override
    public UIElement pre_add(UIElement.Type type, boolean is_toggled, boolean is_focused) {
        UIElement element =  elements.get(type);
        if(element != null && is_toggled){
            elementOrder.remove(type);
            elementOrder.add(type);
            boolean active = element.getActive();
            element.toggle_active();
            //??? which must be true??
            if(active != element.getActive()){
                if(element.getActive()){
                    if(type == UIElement.Type.WORLDMAP)
                        SoundPlayer.play("WorldmapOpen", "WorldmapOpen");
                    else
                        SoundPlayer.play("MenuUp", "MenuUp");

                }
                else{
                    if(type == UIElement.Type.WORLDMAP)
                        SoundPlayer.play("WorldmapClose", "WorldmapClose");
                    else
                        SoundPlayer.play("MenuDown", "MenuDown");
                    if(draggedIcon != null){
                        if(element.getType() == icon_map.get(draggedIcon.type)){
//                            remove_icon();
                        }
                    }

                }
            }
            return element;
        }
        else{
            remove(type);
            elementOrder.add(type);

            if (is_focused)
                focused = type;

            return elements.get(type);
        }
    }
    static HashSet<UIElement.Type> silent_types =
            new HashSet<>(List.of(new UIElement.Type[]{
                    UIElement.Type.STATUSMESSENGER,
                    UIElement.Type.STATUSBAR,
                    UIElement.Type.CHATBAR,
                    UIElement.Type.MINIMAP,
                    UIElement.Type.BUFFLIST,
                    UIElement.Type.NPCTALK,
                    UIElement.Type.SHOP
            }));
    public void emplace(UIElement obj){
        UIElement element = pre_add(obj.getType(), obj.getToggle(), obj.getFocus());
        elements.put(obj.getType(), obj);
        if(!silent_types.contains(element.getType())){
            if (element.getType() == UIElement.Type.WORLDMAP)
                SoundPlayer.play("WorldmapOpen", "WorldmapOpen");
            else
                SoundPlayer.play("MenuUp", "MenuUp");
        }
    }
}
