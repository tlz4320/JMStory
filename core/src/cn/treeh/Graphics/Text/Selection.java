package cn.treeh.Graphics.Text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import java.awt.*;

public class Selection extends Widget{
    Text text;
    boolean underLine;
    public Selection(String string, Text.Font font, int width){
        text = new Text(string, font, width);
        underLine = false;
    }
    public Vector2 draw(Vector2 position, Batch batch) {
        return text.draw(position, batch, underLine);
    }
}
