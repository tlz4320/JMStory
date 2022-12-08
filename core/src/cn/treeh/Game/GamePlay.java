package cn.treeh.Game;

import cn.treeh.UI.UI;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GamePlay {
    Stage stage;
    Batch batch;
    static GamePlay instance;

    public static GamePlay getInstance() {
        return instance;
    }
    public static GamePlay createGamePlay(Stage s, Batch b){
        instance = new GamePlay(s, b);
        return instance;
    }
    private GamePlay(Stage s, Batch b){
        stage = s;
        batch = b;
    }
    public void draw(){

    }
    public void dispose(){

    }
}
