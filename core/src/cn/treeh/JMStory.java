package cn.treeh;

import cn.treeh.Graphics.Window;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.ScreenUtils;

public class JMStory extends ApplicationAdapter {

    SpriteBatch batch;
    Texture img;
    ShaderProgram shaderProgram;
    @Override
    public void create() {
        //shader我还是没搞明白，如果位置不会出问题的话  我决定先不搞了
        //真不行就直接在代码里计算位置好了 不要在shader里面计算
//        shaderProgram = Window.get().shaderInit();
        batch = new SpriteBatch();
//        batch.setShader(shaderProgram);
        img = new Texture("badlogic.jpg");
    }

    @Override
    public void render() {
        ScreenUtils.clear(1, 0, 0, 1);
        batch.begin();
        batch.draw(img, 0, 0);
        batch.end();
    }

    @Override
    public void dispose() {
        batch.dispose();
        img.dispose();
    }

}
