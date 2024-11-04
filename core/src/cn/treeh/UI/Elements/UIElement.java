package cn.treeh.UI.Elements;

import cn.treeh.Graphics.Sprite;
import cn.treeh.Graphics.Text;
import cn.treeh.UI.Component.Button;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;

public class UIElement{

    public enum Type
    {
        NONE,
        START,
        LOGIN,
        TOS,
        GENDER,
        WORLDSELECT,
        REGION,
        CHARSELECT,
        LOGINWAIT,
        RACESELECT,
        CLASSCREATION,
        SOFTKEYBOARD,
        LOGINNOTICE,
        LOGINNOTICE_CONFIRM,
        STATUSMESSENGER,
        STATUSBAR,
        CHATBAR,
        BUFFLIST,
        NOTICE,
        NPCTALK,
        SHOP,
        STATSINFO,
        ITEMINVENTORY,
        EQUIPINVENTORY,
        SKILLBOOK,
        QUESTLOG,
        WORLDMAP,
        USERLIST,
        MINIMAP,
        CHANNEL,
        CHAT,
        CHATRANK,
        JOYPAD,
        EVENT,
        KEYCONFIG,
        OPTIONMENU,
        QUIT,
        CHARINFO,
        CASHSHOP,
        NUM_TYPES
    }
    Stage stage;
    SpriteBatch batch;

    public UIElement(SpriteBatch b, Stage s){
        batch = b;
        stage = s;
    }
    int[] position = new int[2];
    int[] dimension = new int[2];

    public boolean inRange(int[] pos)
    {
        return (pos[0] > position[0] && pos[0] < position[0] + dimension[0]) &&
                (pos[1] > position[1] && pos[1] < position[1] + dimension[1]);

    }
    LinkedList<Sprite> Sprites = new LinkedList<>();
    LinkedList<Button> Buttons = new LinkedList<>();
//    LinkedList<> Buttons = new LinkedList();
    public void pushSprite(Sprite s){
        Sprites.add(s);
    }
    public void pushButton(Button b){
        Buttons.add(b);
    }
    void draw_sprites(float alpha)
    {
        for (Sprite sprite : Sprites)
        {
            sprite.draw(position, alpha, batch);
        }
    }
    void draw_buttons(float alpha){
        for(Button button : Buttons){
            button.draw(position);
        }
    }

    public void draw(float alpha) {
        draw_sprites(alpha);
        draw_buttons(alpha);
    }


    public void dispose() {
        Sprites.clear();
        Buttons.clear();
    }


    public void update() {
        for (Sprite sprite : Sprites)
        {
            sprite.update();
        }
    }
    boolean active;
    public void setActive()
    {
        active = true;
    }

    public void deActive()
    {
        active = false;
    }

    public boolean getActive()
    {
        return active;
    }
    public void send_key(int keycode, boolean pressed, boolean escape){}
    public void toggle_active()
    {
        if (active)
            deActive();
        else
            setActive();
    }
    public boolean getToggle(){
        return false;
    }
    public boolean getFocus(){
        return false;
    }
    public Type getType(){
        return Type.NONE;
    }
}
