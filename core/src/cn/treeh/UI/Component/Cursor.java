package cn.treeh.UI.Component;

import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Cursor;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Graphics;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lwjgl.glfw.GLFW;

import static org.lwjgl.glfw.GLFW.*;

public class Cursor {
    static Cursor instance;
    public static Cursor get(){
        if(instance == null){
            instance = new Cursor();
            instance.init();
        }
        return instance;
    }
    enum State
    {
        IDLE(0),
        CANCLICK(1),
        GAME(2),
        HOUSE(3),
        CANCLICK2(4),
        CANGRAB(5),
        GIFT(6),
        VSCROLL(7),
        HSCROLL(8),
        VSCROLLIDLE(9),
        HSCROLLIDLE(10),
        GRABBING(11),
        CLICKING(12),
        RCLICK(13),
        LEAF(18),
        CHATBARVDRAG(67),
        CHATBARHDRAG(68),
        CHATBARBLTRDRAG(69),
        CHATBARMOVE(72),
        CHATBARBRTLDRAG(73),
        LENGTH(74);
        public final int id;
        State(int i) {
            this.id = i;
        }

    };

    private Cursor() {
        state = State.IDLE;
        hide_counter = 0;
    }

    public void init() {
        glfwSetInputMode(((Lwjgl3Graphics) Gdx.graphics).getWindow().getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
        Node node = NXFiles.UI().subNode("Basic.img/Cursor");
        animations = new Animation[node.nChild()];
        for(int i = 0; i < node.nChild(); i++){
            animations[i] = new Animation(node.subNode("" + i));
        }
    }

    public void draw(float alpha, SpriteBatch batch) {
        animations[state.id].draw(new DrawArg(new int[]{Gdx.input.getX(), Gdx.input.getY()}), alpha, batch);
    }
    public void update() {

    }
    public void setState(State state){
        this.state = state;
    }
    public void setPosition(int[] cursor_position){
        this.position = cursor_position;
    }
    public State getState(){
        return state;
    }
    int[] getPosition() {
        return position;
    }
    Animation[] animations;

    State state;
    int[] position;
    int hide_counter;

    static int HIDE_TIME = 15000;
}
