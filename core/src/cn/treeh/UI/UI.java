package cn.treeh.UI;

import java.awt.*;

public class UI {
    static UI ui;
    public static UI get(){
        if(ui == null)
            ui = new UI();
        return ui;
    }
    public void sendKey(int key, boolean pressed){

    }
    public void sendCursor(boolean pressed){}
    public void doubleClick(){}
    public void sendCursor(Point point){}
}
