package cn.treeh.UI.Display;

import cn.treeh.Audio.BgmPlayer;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.UI.UI;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class LoginDisplay extends BaseDisplay{
    Stage stage;
    Batch batch;
    public LoginDisplay(Stage s, Batch b){
        stage = s;
        batch = b;
        BgmPlayer.play("BgmUI.img/Title");
        Node title = NXFiles.UI().subNode("Login.img/Title");
        Node common = NXFiles.UI().subNode("Login.img/Common");
    }
    @Override
    public void draw() {

    }

    @Override
    public void dispose() {

    }
}
