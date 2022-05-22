package cn.treeh.Graphics;


import com.badlogic.gdx.graphics.glutils.ShaderProgram;

import java.awt.*;
import java.nio.IntBuffer;

public class GraphicsGL {
    static GraphicsGL graphicsGL;
    public static GraphicsGL get() {
        if (graphicsGL == null)
            graphicsGL = new GraphicsGL();
        return graphicsGL;
    }
    public void initFont(){

    }
}
