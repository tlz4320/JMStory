package cn.treeh.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import static org.lwjgl.opengl.GL20.glUniform1i;
import static org.lwjgl.opengl.GL20.glUniform2f;

public class MBatch extends SpriteBatch {
    ShaderProgram shaderProgram;
    public MBatch(){
        super();
        String vertexShader = Gdx.files.internal("vshader.glsl").readString();
        String fragmentShader = Gdx.files.internal("fshader.glsl").readString();
        shaderProgram = new ShaderProgram(vertexShader,fragmentShader);
        setShader(shaderProgram);
    }
    public void init(){

    }
    @Override
    public void setupMatrices(){
//        super.setupMatrices();
    }

}
