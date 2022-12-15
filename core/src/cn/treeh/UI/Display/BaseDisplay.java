package cn.treeh.UI.Display;

import cn.treeh.Graphics.Sprite;
import cn.treeh.Graphics.Text;
import cn.treeh.UI.Component.Button;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.LinkedList;

public class BaseDisplay implements Display{

    Stage stage;
    SpriteBatch batch;

    public BaseDisplay(SpriteBatch b, Stage s){
        batch = b;
        stage = s;
    }
    int[] position = new int[2];
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
    @Override
    public void draw(float alpha) {
        draw_sprites(alpha);
        draw_buttons(alpha);
    }

    @Override
    public void dispose() {
        Sprites.clear();
        Buttons.clear();
    }

    @Override
    public void update() {
        for (Sprite sprite : Sprites)
        {
            sprite.update();
        }
    }
}
