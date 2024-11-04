package cn.treeh.UI.Component.Tooltip;

import com.badlogic.gdx.Gdx;

public abstract class Tooltip {
    public enum Parent
    {
        NONE,
        EQUIPINVENTORY,
        ITEMINVENTORY,
        SKILLBOOK,
        SHOP,
        EVENT,
        TEXT,
        KEYCONFIG,
        WORLDMAP,
        MINIMAP
    };

    public abstract void draw(int x, int y);
    public void draw(int[] curPos){
        draw(curPos[0], curPos[1]);
    }
    public void draw(){
        draw(Gdx.input.getX(), Gdx.input.getY() + 22);
    }
}
