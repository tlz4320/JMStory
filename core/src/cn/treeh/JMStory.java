package cn.treeh;

import cn.treeh.Game.GamePlay;
import cn.treeh.Graphics.*;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.UI.UI;
import cn.treeh.Util.Configure;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;

import static org.lwjgl.opengl.GL.createCapabilities;
import static org.lwjgl.opengl.GL20.*;

public class JMStory extends ApplicationAdapter {

//    private Viewport viewport;
    ShapeRenderer sr;
    SpriteBatch batch;
    Stage stage;
    UI ui;
    GamePlay game;
    TextureRegion texture;
    int time_step = Configure.TIME_STEP * 1000;
    int accumulator = time_step;

    long last_time = 0;
    @Override
    public void create() {
//        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new PerspectiveCamera());
        stage = new Stage();
        sr = new ShapeRenderer();
        batch = new MBatch();
        Gdx.input.setInputProcessor(stage);
//        last_time = System.currentTimeMillis();
//        BgmPlayer.play("BgmUI.img/Title");
//        ui = UI.createUI(batch, stage);
        game = GamePlay.createGamePlay(batch, stage);
        game.load(100050000, 1);
//
    }



    int frame = 0;

    void update()
    {

        game.update();
//        ui.update();

    }

    void draw(float alpha)
    {
//        Window::get().begin();
//        Stage::get().draw(alpha);
        game.draw(alpha);
//        ui.draw(alpha);

//        Window::get().end();
    }

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
        long elapsed = last_time;
        last_time = System.currentTimeMillis();
        elapsed = (last_time - elapsed);
        update();
        float alpha = (float) elapsed/ time_step;
        batch.begin();
        draw(alpha);
        batch.end();
        stage.act();
//        stage.act();
//        stage.draw();
    }
//    public void reshape(int width, int height){
//        viewport.update(width, height);
//    }
    @Override
    public void dispose() {
        batch.dispose();
        ui.dispose();
        game.dispose();
    }

}