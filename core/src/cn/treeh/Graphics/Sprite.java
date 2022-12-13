package cn.treeh.Graphics;

public class Sprite {
    Animation animation;
    DrawArg arg;
    public Sprite(Animation _a, DrawArg _d){
        animation = _a;
        arg = _d;
    }
    public void draw(int[] parent_pos, float alpha){
        arg.addPos(parent_pos);
        animation.draw(arg, alpha);
    }
    public boolean update(){
        return animation.update();
    }
}
