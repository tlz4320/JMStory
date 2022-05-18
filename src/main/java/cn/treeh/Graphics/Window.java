package cn.treeh.Graphics;

import cn.treeh.UI.UI;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;

import java.awt.*;
import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.stackPush;
import static org.lwjgl.system.MemoryUtil.NULL;

public class Window {
    int WIDTH  = 300;
    int HEIGHT = 300;
    static Window window;
    static long windowPos;
    public static Window get(){
        if(window == null)
            window = new Window();
        return window;
    }

    public void init(){

        GLFWErrorCallback.createPrint().set();
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize glfw");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_FALSE);




        glfwWindowHint(GLFW_VISIBLE, GL_TRUE);
        windowPos = glfwCreateWindow(WIDTH, HEIGHT,
                "JMaple", NULL, NULL);

        glfwMakeContextCurrent(windowPos);
        glfwSwapInterval(1);
        glfwShowWindow(windowPos);
        GraphicsGL.get().init();
        glViewport(0, 0, WIDTH, HEIGHT);
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glfwSetInputMode(windowPos, GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        glfwSetInputMode(windowPos, GLFW_STICKY_KEYS, 1);
        glfwSetKeyCallback(windowPos, (windowHnd, key, scancode, action, mods) -> {
            UI.get().sendKey(key, action != GLFW_RELEASE);
        });
        glfwSetMouseButtonCallback(windowPos, (windowHnd, button, action, mods) -> {
            switch (button){
                case GLFW_MOUSE_BUTTON_LEFT:
                    switch (action){
                        case GLFW_PRESS:
                            UI.get().sendCursor(true);
                            break;
                        case GLFW_RELEASE:
                            UI.get().sendCursor(false);
                            break;
                    }
                    break;
                case GLFW_MOUSE_BUTTON_RIGHT:
                    if (action == GLFW_PRESS) {
                        UI.get().doubleClick();
                    }
                    break;
            }
        });
        glfwSetCursorPosCallback(windowPos, (windowHnd, xpos, ypos) -> {
            UI.get().sendCursor(new Point((int) xpos, (int) ypos));
        });
        GLCapabilities gles = createCapabilities();

        System.out.println("GL_VENDOR: " + glGetString(GL_VENDOR));
        System.out.println("GL_VERSION: " + glGetString(GL_VERSION));
        System.out.println("GL_RENDERER: " + glGetString(GL_RENDERER));

        // Render with OpenGL ES
//        glfwShowWindow(windowPos);
//
//        glClearColor(0.0f, 0.5f, 1.0f, 0.0f);
//        while (!glfwWindowShouldClose(windowPos)) {
//            glfwPollEvents();
//
//            glClear(GL_COLOR_BUFFER_BIT);
//            glfwSwapBuffers(windowPos);
//        }
//
//        GLES.setCapabilities(gles);
//
//        glfwFreeCallbacks(windowPos);
//        glfwTerminate();


        // EGL capabilities
//        long dpy = glfwGetEGLDisplay();
//
//        EGLCapabilities egl;
//        try (MemoryStack stack = stackPush()) {
//            IntBuffer major = stack.mallocInt(1);
//            IntBuffer minor = stack.mallocInt(1);
//
//            if (!eglInitialize(dpy, major, minor)) {
//                throw new IllegalStateException(String.format("Failed to initialize EGL [0x%X]", eglGetError()));
//            }
//
//            egl = EGL.createDisplayCapabilities(dpy, major.get(0), minor.get(0));
//        }
    }
}
