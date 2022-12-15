package cn.treeh.UI;

import cn.treeh.UI.Display.Display;
import cn.treeh.UI.Display.LoginDisplay;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.*;

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
    private UI(SpriteBatch b, Stage s){
        stage = s;
        batch = b;
        display = new LoginDisplay(b,s);
    }
    Display display;
    public void draw(float alpha){
        display.draw(alpha);
    }
    public void update(){
        display.update();
    }
    public void dispose(){
        display.dispose();
    }
}
