package cn.treeh.UI.Component;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Component extends Actor {
    SpriteBatch batch;
    Stage stage;
    public Component(SpriteBatch b, Stage s){
        batch = b;
        stage = s;
    }
}
