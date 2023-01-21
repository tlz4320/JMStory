package cn.treeh.UI.Component;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public abstract class MapleButton extends Button{
    Stage stage;
    Texture[] texture = new Texture[State.NUM_STATES.ordinal()];
    public MapleButton(SpriteBatch b, Stage stage){
        super(b, stage);
        this.stage = stage;
    }
    DrawArg drawArg;
    public void addListener(){
        addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
//                if (!super.touchDown(event, x, y, pointer, button)) return false;
                setState(State.PRESSED);
//                Cursor.get().setState(Cursor.State.CANCLICK);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
                callback();
//                Cursor.get().setState(Cursor.State.CANCLICK);
                setState(State.MOUSEOVER);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
                if (toActor == null || !isOver(toActor, x,y)){
                    setState(State.NORMAL);
                }
//                Cursor.get().setState(Cursor.State.IDLE);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(!isPressed()) {
                    setState(State.MOUSEOVER);
//                    Cursor.get().setState(Cursor.State.CANCLICK);
                }
            }
        });
    }
    public MapleButton(Node src, int[] pos,  SpriteBatch b, Stage stage){
        super(b, stage);
        texture[State.PRESSED.ordinal()] = new Texture(src.subNode("pressed/0"));
        texture[State.MOUSEOVER.ordinal()] = new Texture(src.subNode("mouseOver/0"));
        texture[State.NORMAL.ordinal()] = new Texture(src.subNode("normal/0"));
        texture[State.DISABLED.ordinal()] = new Texture(src.subNode("disabled/0"));
        position = pos;
        drawArg = new DrawArg(pos);
        state = State.NORMAL;
        active = true;

        addListener();
        stage.addActor(this);
    }
    public void draw(int[] parentpos)
    {
        if (active)
        {

            drawArg.addPos(parentpos);
            Texture toDraw = texture[state.ordinal()];
            setBounds(drawArg.pos[0], drawArg.pos[1], toDraw.dimensions[0], toDraw.dimensions[1]);
            toDraw.draw(drawArg, batch);
        }
    }
}
