package cn.treeh.Graphics;

import cn.treeh.Util.Configure;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.BufferUtils;

import static com.badlogic.gdx.graphics.GL20.GL_FLOAT;
import static com.badlogic.gdx.graphics.GL20.GL_SHORT;

public class Window {
    static Window window;
    public static Window get(){
        if(window == null)
            window = new Window();
        return window;
    }

//    public ShaderProgram shaderInit(){

//        String vs_source = Gdx.files.internal("vshader.glsl").readString();
//        String fs_source = Gdx.files.internal("fshader.glsl").readString();;
//        ShaderProgram res = new ShaderProgram(vs_source, fs_source);
//
//        res.setUniformf("yoffset", Configure.VIEW_Y_OFFSET);
//        res.setUniformf("fontregion", 800);
//        res.setUniformf("atlassize", new Vector2(8192, 8192));
//        res.setUniformf("screensize", new Vector2(Configure.screenWidth, Configure.screenHeight));
//        return res;
//    }
}
