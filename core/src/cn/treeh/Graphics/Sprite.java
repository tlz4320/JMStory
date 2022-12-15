package cn.treeh.Graphics;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Sprite {
    Animation animation;
    DrawArg arg;
    public Sprite(Animation _a, DrawArg _d){
        animation = _a;
        arg = _d;
    }
    public void draw(int[] parent_pos, float alpha,  SpriteBatch batch){
        arg.addPos(parent_pos);
        animation.draw(arg, alpha, batch);
    }
    public boolean update(){
        return animation.update();
    }
}
