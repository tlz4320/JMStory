package cn.treeh.UI;

import cn.treeh.UI.Display.Display;
import cn.treeh.UI.Display.LoginDisplay;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.awt.*;

public class UI {
    static UI instance;

    public static UI getInstance() {
        return instance;
    }
    public static UI createUI(Stage s, Batch b){
        instance = new UI(s, b);
        return instance;
    }
    Stage stage;
    Batch batch;
    private UI(Stage s, Batch b){
        stage = s;
        batch = b;
        display = new LoginDisplay(s, b);
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
