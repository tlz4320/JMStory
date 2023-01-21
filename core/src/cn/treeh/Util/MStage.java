package cn.treeh.Util;

import cn.treeh.UI.UI;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Null;
import com.badlogic.gdx.utils.Pools;

public class MStage extends Stage {
    @Override
    public boolean keyDown(int keyCode) {


        Actor target = getKeyboardFocus();
        if(target == null){
            UI.getInstance().sendKey(keyCode, true);
            target = getRoot();
        }
        InputEvent event = Pools.obtain(InputEvent.class);
        event.setStage(this);
        event.setType(InputEvent.Type.keyDown);
        event.setKeyCode(keyCode);
        target.fire(event);
        boolean handled = event.isHandled();
        Pools.free(event);
        return handled;
    }

    @Override
    public boolean keyUp(int keyCode) {
        UI.getInstance().sendKey(keyCode, false);
        return super.keyUp(keyCode);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        setKeyboardFocus(null);
        return super.touchDown(screenX, screenY, pointer, button);
    }

    @Override
    public Vector2 screenToStageCoordinates(Vector2 screenCoords) {
        screenCoords.y = Configure.screenHeight - screenCoords.y;
        return super.screenToStageCoordinates(screenCoords);
    }

}
