package cn.treeh;

import cn.treeh.Graphics.LineDrawer;
import cn.treeh.Graphics.Text.Text;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class JMStory extends ApplicationAdapter {

    private Viewport viewport;
    private Camera camera;
    ShapeRenderer sr;
    SpriteBatch batch;
    Texture img;
    Stage stage;
    ShaderProgram shaderProgram;
    Label label;
    @Override
    public void create() {
        camera = new PerspectiveCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        //shader我还是没搞明白，如果位置不会出问题的话  我决定先不搞了
        //真不行就直接在代码里计算位置好了 不要在shader里面计算
//        shaderProgram = Window.get().shaderInit();
        Label.LabelStyle sp = new Label.LabelStyle(Text.fontMap.get(Text.Font.A11M), Color.BLUE);
        stage = new Stage();
        Gdx.input.setInputProcessor(stage);
        label = new Label("Fuck", sp);
        label.setPosition(100, 100);
        sr = new ShapeRenderer();
        batch = new SpriteBatch();

        img = new Texture("badlogic.jpg");
    }
    int frame = 0;
    @Override
    public void render() {
        frame++;
        ScreenUtils.clear(1, 1, 1, 1);
        //我在想需不需要每一帧都单独进行一个input的更新
        //现在延迟30帧更新一次好了，减少没必要的计算
        if(frame == 30) {
            for(Actor actor : stage.getActors()) {
                if(!actor.isVisible())
                    actor.remove();
            }
            stage.draw();
            frame = 0;
        }
        stage.act();

        batch.begin();
        label.draw(batch, 1);

        LineDrawer.draw(batch, 100F, 100F, 100 + label.getPrefWidth(), 100F, 1);
        batch.end();


    }
    public void reshape(int width, int height){
        viewport.update(width, height);
    }
    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

}
