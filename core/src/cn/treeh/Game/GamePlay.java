package cn.treeh.Game;

import cn.treeh.Game.MapleMap.Map;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.UI.UI;
import cn.treeh.Util.StringUtil;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class GamePlay{
    Stage stage;
    Batch batch;
    static GamePlay instance;

    Map map;
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
        stage.addListener(new InputListener(){
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                return super.keyDown(event, keycode);
            }
        });
    }
    public void draw(){

    }
    public void dispose(){

    }

    public void load_map(int mapId)
    {

    }

}
