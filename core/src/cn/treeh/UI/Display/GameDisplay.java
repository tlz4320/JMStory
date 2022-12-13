package cn.treeh.UI.Display;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GameDisplay extends BaseDisplay{
    Stage stage;
    Batch batch;
    public GameDisplay(Stage s, Batch b){
        stage = s;
        batch = b;
    }
    @Override
    public void draw(float alpha) {

    }

    @Override
    public void dispose() {

    }
}
