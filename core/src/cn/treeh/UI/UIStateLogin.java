package cn.treeh.UI;

import cn.treeh.IO.Key.KeyType;
import cn.treeh.UI.Component.Icon;
import cn.treeh.UI.Component.Tooltip;
import cn.treeh.UI.Elements.Login;
import cn.treeh.UI.Elements.UIElement;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class UIStateLogin implements UIState{
    UIElement.Type focused;
    public UIStateLogin(SpriteBatch b, Stage s){
        focused = UIElement.Type.NONE;
        elements.put(UIElement.Type.LOGIN, new Login(b, s));
    }
    HashMap<UIElement.Type, UIElement> elements = new HashMap<>();
    Tooltip tooltip;
    @Override
    public void draw(float inter, int[] cursor) {
        for (Map.Entry<UIElement.Type, UIElement> element : elements.entrySet())
        {

            if (element.getValue().getActive())
                element.getValue().draw(inter);
        }

        if (tooltip != null)
            tooltip.draw(cursor);
    }

    @Override
    public void update() {
        for (Map.Entry<UIElement.Type, UIElement> element : elements.entrySet())
        {

            if (element.getValue().getActive())
                element.getValue().update();

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

    }

    @Override
    public void remove(UIElement.Type type) {

    }

    @Override
    public UIElement get(UIElement.Type type) {
        return null;
    }

    @Override
    public UIElement get_front(LinkedList<UIElement.Type> types) {
        return null;
    }

    @Override
    public UIElement get_front(int[] cursor_position) {
        return null;
    }
}
