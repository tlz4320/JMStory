package cn.treeh;

import cn.treeh.Graphics.Text;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class JMStory extends ApplicationAdapter {

    private Viewport viewport;
    private Camera camera;
    SpriteBatch batch;
    Texture img;
    ShaderProgram shaderProgram;
    @Override
    public void create() {
        camera = new PerspectiveCamera();
        viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), camera);
        //shader我还是没搞明白，如果位置不会出问题的话  我决定先不搞了
        //真不行就直接在代码里计算位置好了 不要在shader里面计算
//        shaderProgram = Window.get().shaderInit();
        batch = new SpriteBatch();
//        batch.setShader(shaderProgram);
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 1, 1, 1);

        batch.begin();
        BitmapFont bt = Text.fontMap.get(Text.Font.A11M);
        bt.setColor(Color.RED);
        GlyphLayout l = bt.draw(batch, "车是测试22", 400, 400);

        bt.setColor(Color.BLUE);
        bt.draw(batch, "车是测试22", 400, 500, 6 * 11, Align.right, true);
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
