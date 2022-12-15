package cn.treeh.UI.Display;

import cn.treeh.Audio.BgmPlayer;
import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Sprite;
import cn.treeh.Graphics.Text;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.UI.Component.MapleButton;
import cn.treeh.UI.Component.TextInput;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class LoginDisplay extends BaseDisplay{

    public void initButton(){
        Node title = NXFiles.UI().subNode("Login.img/Title");
        pushButton(new MapleButton(title.subNode("BtLogin"), new int[]{626, 278}, batch, stage) {
            @Override
            public boolean callback() {
                return false;
            }
        });
//        buttons[BT_REGISTER] = std::make_unique<MapleButton>(title["BtNew"], Point<int>(309, 320));
//        buttons[BT_HOMEPAGE] = std::make_unique<MapleButton>(title["BtHomePage"], Point<int>(382, 320));
//        buttons[BT_PASSLOST] = std::make_unique<MapleButton>(title["BtPasswdLost"], Point<int>(470, 300));
//        buttons[BT_QUIT] = std::make_unique<MapleButton>(title["BtQuit"], Point<int>(455, 320));
//        buttons[BT_IDLOST] = std::make_unique<MapleButton>(title["BtLoginIDLost"], Point<int>(395, 300));
//        buttons[BT_SAVEID] = std::make_unique<MapleButton>(title["BtLoginIDSave"], Point<int>(325, 300));

    }
    TextField account, password;
    public LoginDisplay(SpriteBatch b, Stage s){
        super(b, s);
        BgmPlayer.play("BgmUI.img/Title");
        Node login_map = NXFiles.Map().subNode("Obj/login.img");
        Node title = login_map.subNode("Title");
        Node common = NXFiles.UI().subNode("Login.img/Common");
        pushSprite(new Sprite(new Animation(login_map.subNode("WorldSelect/AncientTreasure/0")), new DrawArg(new int[]{0, 0})));
        pushSprite(new Sprite(new Animation(title.subNode("Logo")), new DrawArg(new int[]{0, 0})));
        Node signboard = title.subNode("signboard");
        pushSprite(new Sprite(new Animation(signboard.subNode("1").subNode(0)), new DrawArg(new int[]{400, 200})));
        pushSprite(new Sprite(new Animation(signboard.subNode("4").subNode(0)), new DrawArg(new int[]{460, 355})));
        pushSprite(new Sprite(new Animation(signboard.subNode("6").subNode(0)), new DrawArg(new int[]{400, 150})));
//
        pushSprite(new Sprite(new Animation(common.subNode("frame")) ,new DrawArg(new int[] {0, 0})));
        initButton();
        password = TextInput.createTextInput("", Text.bitmapFont);
        password.setMessageText("");
        password.setPasswordCharacter('*');
        password.setPasswordMode(true);
        password.setPosition(485, 286);
        account = TextInput.createTextInput("", Text.bitmapFont);
        account.setPosition(485, 316);
        stage.addActor(password);
        stage.addActor(account);
    }
    @Override
    public void draw(float alpha) {
        super.draw(alpha);
        password.draw(batch, 1);
        account.draw(batch, 1);
    }

    @Override
    public void dispose() {

    }
}
