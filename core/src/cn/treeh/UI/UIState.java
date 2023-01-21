package cn.treeh.UI;

import cn.treeh.IO.Key.KeyType;
import cn.treeh.UI.Component.Icon;
import cn.treeh.UI.Component.Tooltip;
import cn.treeh.UI.Elements.UIElement;

import java.util.LinkedList;

public interface UIState {
    public void draw(float inter, int[] cursor);
    public void update();
    public void send_key(KeyType type, int action, boolean pressed, boolean escape);
    public void send_scroll(double yoffset);
    public void send_close();

    public void drag_icon(Icon icon);
    public void clear_tooltip(Tooltip.Parent parent);
    public void show_equip(Tooltip.Parent parent, int slot);
    public void show_item(Tooltip.Parent parent, int itemid);
    public void show_skill(Tooltip.Parent parent, int skill_id, int level, int masterlevel, int expiration);
    public void show_text(Tooltip.Parent parent, String text);
    public void show_map(Tooltip.Parent parent, String name, String description, int mapid, boolean bolded, boolean portal);

    public void remove(UIElement.Type type);
    public UIElement get(UIElement.Type type);
    public UIElement get_front(LinkedList<UIElement.Type> types);
    public UIElement get_front(int[] cursor_position);
}
