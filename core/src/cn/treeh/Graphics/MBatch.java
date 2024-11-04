package cn.treeh.Graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.glBindBuffer;
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
//        glEnable(GL_BLEND);
//		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
//		glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
    }
    @Override
    public void setupMatrices(){
//        super.setupMatrices();
    }

}
