package cn.treeh.UI;

import cn.treeh.IO.Key.KeyMapping;
import cn.treeh.IO.Key.Keyboard;
import cn.treeh.UI.Elements.UIElement;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.HashSet;
import java.util.LinkedList;

public class UI {
    static UI instance;

    public static UI getInstance() {
        return instance;
    }
    public static UI createUI(SpriteBatch b, Stage s){
        instance = new UI(b,s);
        return instance;
    }
    Stage stage;
    SpriteBatch batch;
    boolean caps_locked = false;
    private UI(SpriteBatch b, Stage s){
        stage = s;
        batch = b;
        display = new UIStateLogin(b,s);
    }
    HashSet<UIElement.Type> types = new HashSet<>();
    public void sendKey(int key, boolean pressed) {
        if (Gdx.input.isKeyPressed(Input.Keys.ALT_LEFT) && Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            //缩放屏幕
            return;
        }
        caps_locked = Gdx.input.isKeyJustPressed(Input.Keys.CAPS_LOCK) != caps_locked;

        KeyMapping mapping = Keyboard.getMapping(key);
        types.clear();
        boolean sent = false;


        boolean escape = key == Input.Keys.ESCAPE;
        boolean tab = key == Input.Keys.TAB;
        boolean enter = key == Input.Keys.ENTER || key == Input.Keys.NUMPAD_ENTER;
        boolean up_down = key == Input.Keys.UP || key == Input.Keys.DOWN;
        boolean left_right = key == Input.Keys.LEFT || key == Input.Keys.RIGHT;
        boolean arrows = up_down || left_right;

        UIElement status_bar = display.get(UIElement.Type.STATUSBAR);
        UIElement channel = display.get(UIElement.Type.CHANNEL);

        if (status_bar != null && status_bar.getActive()) {
            status_bar.send_key(mapping.getAction(), pressed, escape);
        } else {
            if (escape || tab || enter || arrows) {
                // Login
                types.add(UIElement.Type.WORLDSELECT);
                types.add(UIElement.Type.CHARSELECT);
                types.add(UIElement.Type.RACESELECT);            // No tab
                types.add(UIElement.Type.CLASSCREATION);            // No tab (No arrows, but shouldn't send else where)
                types.add(UIElement.Type.LOGINNOTICE);            // No tab (No arrows, but shouldn't send else where)
                types.add(UIElement.Type.LOGINNOTICE_CONFIRM);    // No tab (No arrows, but shouldn't send else where)
                types.add(UIElement.Type.LOGINWAIT);                // No tab (No arrows, but shouldn't send else where)
            }
            if (escape) {
                types.add(UIElement.Type.SOFTKEYBOARD);

                // Game
                types.add(UIElement.Type.NOTICE);
                types.add(UIElement.Type.KEYCONFIG);
                types.add(UIElement.Type.CHAT);
                types.add(UIElement.Type.EVENT);
                types.add(UIElement.Type.STATSINFO);
                types.add(UIElement.Type.ITEMINVENTORY);
                types.add(UIElement.Type.EQUIPINVENTORY);
                types.add(UIElement.Type.SKILLBOOK);
                types.add(UIElement.Type.QUESTLOG);
                types.add(UIElement.Type.USERLIST);
                types.add(UIElement.Type.NPCTALK);
                types.add(UIElement.Type.CHARINFO);
            } else if (enter) {
                // Login
                types.add(UIElement.Type.SOFTKEYBOARD);

                // Game
                types.add(UIElement.Type.NOTICE);
            } else if (tab) {
                // Game
                types.add(UIElement.Type.ITEMINVENTORY);
                types.add(UIElement.Type.EQUIPINVENTORY);
                types.add(UIElement.Type.SKILLBOOK);
                types.add(UIElement.Type.QUESTLOG);
                types.add(UIElement.Type.USERLIST);
            }

            if (types.size() > 0) {
                UIElement element = display.get_front(types);

                if (element != null) {
                    element.send_key(mapping.getAction(), pressed, escape);
                    sent = true;
                }
            }
        }
        if (!sent) {
            if (escape) {
                display.send_key(mapping.getType(), mapping.getAction(), pressed, escape);
            } else if (enter) {
                UIElement chat_bar = display.get(UIElement.Type.CHATBAR);
                if (chat_bar != null)
                    chat_bar.send_key(mapping.getAction() , pressed, escape);
                else
                    display.send_key(mapping.getType(), mapping.getAction(), pressed, escape);

            } else {
                display.send_key(mapping.getType(), mapping.getAction(), pressed, escape);
            }
        }

    }
    UIState display;
    public void draw(float alpha){
        display.draw(alpha, batch);
    }
    public void update(){
        display.update();
    }
    public void dispose(){
//        display.dispose();
    }
    public void remove(UIElement.Type type){
        display.remove(type);
    }
    public UIState getState(){
        return(display);
    }
}
