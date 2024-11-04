package cn.treeh.UI.Elements;

import cn.treeh.Game.Player.CharStat;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class UIStatusBar extends UIElement {
    public boolean getToggle(){
        return true;
    }
    public boolean getFocus(){
        return false;
    }
    public Type getType(){
        return Type.STATUSBAR;
    }
    public UIStatusBar(SpriteBatch b, Stage s, CharStat stat) {
        super(b, s);

    }
}
