package cn.treeh;

import cn.treeh.Util.FontAddImg;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.TextraLabel;

public class JMStory extends ApplicationAdapter {

    private Viewport viewport;
    private Camera camera;
    ShapeRenderer sr;
    SpriteBatch batch;
    Texture img;
    Stage stage;
    ShaderProgram shaderProgram;
    TextraLabel label;
    @Override
    public void create() {
        camera = new PerspectiveCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        //shader我还是没搞明白，如果位置不会出问题的话  我决定先不搞了
        //真不行就直接在代码里计算位置好了 不要在shader里面计算
//        shaderProgram = Window.get().shaderInit();
//        Label.LabelStyle sp = new Label.LabelStyle(Text_rm.fontMap.get(Text_rm.Font.A11M), Color.BLUE);
        stage = new Stage();
        String text = "[%50][GREEN]Hello,{WAIT} world!"
                + "[ORANGE]{SLOWER}[+Img1] [%200]Did you[] know orange is my favorite color?";

// Create a TypingLabel instance with your custom text

        sr = new ShapeRenderer();
        batch = new SpriteBatch();
//
//        stage.addActor(label);
        Gdx.input.setInputProcessor(stage);
        img = new Texture("badlogic.jpg");
        font = new Font("alibb.fnt").setTextureFilter().setName("alibb");
        FontAddImg.fontAddImg(font, 1, img);
        label = new TextraLabel(text, font);
        label.setPosition(100, 100);

    }
    Font font;
    int frame = 0;
    @Override
    public void render() {
        frame++;
        ScreenUtils.clear(0, 0, 0, 1);
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