package cn.treeh.UI.Component;

import cn.treeh.Graphics.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.github.tommyettinger.textra.TextraButton;

public abstract class Button extends Component {
    public Button(SpriteBatch b, Stage s) {
        super(b, s);
    }

    public static enum State
    {
        NORMAL,
        DISABLED,
        MOUSEOVER,
        PRESSED,
        IDENTITY,
        NUM_STATES
    };

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public int[] getPosition() {
        return position;
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public abstract boolean callback();
    State state;
    int [] position;
    boolean active;

    public void dispose(){
        remove();
    }
    public abstract void draw(int[] parentpos);
}
