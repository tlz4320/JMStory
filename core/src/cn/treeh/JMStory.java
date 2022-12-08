package cn.treeh;

import cn.treeh.Audio.BgmPlayer;
import cn.treeh.Game.GamePlay;
import cn.treeh.NX.Bitmap;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.UI.UI;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.PixmapIO;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.nio.ByteBuffer;

public class JMStory extends ApplicationAdapter {

    private Viewport viewport;
    ShapeRenderer sr;
    SpriteBatch batch;
    Stage stage;
    UI ui;
    GamePlay game;
    Pixmap pixmap;
    Texture texture;
    @Override
    public void create() {
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new PerspectiveCamera());
        stage = new Stage();
        sr = new ShapeRenderer();
        batch = new SpriteBatch();
        Gdx.input.setInputProcessor(stage);
        BgmPlayer.play("BgmUI.img/Title");

        pixmap = new Pixmap(331, 185, Pixmap.Format.RGBA8888);
        ByteBuffer b;

        Node n = NXFiles.Map().subNode("Obj/login.img/Title/signboard/1/0");
        Bitmap bm = n.getBitmap();
        b = bm.data();

        pixmap.setPixels(b);
        PixmapIO.writePNG(Gdx.files.absolute("D:\\program\\project\\JMStory\\wz\\png\\test2.png"), pixmap);
        texture = new Texture(pixmap);
//        ui = UI.createUI(stage, batch);
//        game = GamePlay.createGamePlay(stage, batch);
    }
    int frame = 0;
    @Override
    public void render() {
        frame++;
        ScreenUtils.clear(1, 1, 1, 0);
        //我在想需不需要每一帧都单独进行一个input的更新
        //现在延迟30帧更新一次好了，减少没必要的计算
        if(frame == 30) {
            for(Actor actor : stage.getActors()) {
                if(!actor.isVisible())
                    actor.remove();
            }

            frame = 0;
        }

        batch.begin();
//        ui.draw();
//        game.draw();
        batch.draw(texture, 100, 100);
        batch.end();

//        stage.act();

    }
    public void reshape(int width, int height){
        viewport.update(width, height);
    }
    @Override
    public void dispose() {
        batch.dispose();
        ui.dispose();
        game.dispose();
    }

}
//        label.addListener(new ClickListener(){
//            @Override
//            public void clicked(InputEvent event, float x, float y) {
//                O.ptln("Test");
//            }
//            @Override
//            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                if (toActor == null || !isOver(toActor, x,y))
//                    O.ptln("Out");
//            }
//
//            @Override
//            public void enter(InputEvent event, float x, float y, int pointer, Actor toActor) {
//                if(!this.isPressed())
//                    O.ptln("In");
//            }
//        });