package cn.treeh.Game.Player.Look;

import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Rectangle;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.StringUtil;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class AfterImage {
    Animation animation;
    int first;
    Rectangle range;
    boolean displayed;
    public AfterImage(int skill, String name, String stance, int level){
        Node src = null;
        if(skill > 0){
            String str = StringUtil.extend(skill, 7);
            src = NXFiles.Skill().
                    subNode(str.substring(0, 3))
                    .subNode("skill").subNode(str)
                    .subNode("afterimage").subNode(name)
                    .subNode(stance);
        }
        if(src == null || !src.valid())
            src = NXFiles.Character().subNode("Afterimage")
                    .subNode(name + ".img")
                    .subNode("" + (level / 10))
                    .subNode(stance);
        range = new Rectangle(src);
        first = 0;
        displayed = false;
        for(int i = 0; i < src.nChild(); i++){
            Node sub = src.subNode(i);
            try {
                int frame = Integer.parseInt(sub.getName());
                animation = new Animation(sub);
                first = frame;
            }catch (NumberFormatException ignored){
            }
        }
    }
    public AfterImage(){
        first = 0;
        displayed = true;
    }
    public void draw(int stframe, DrawArg arg, float alpha, SpriteBatch batch){
        if(!displayed && stframe >= first)
            animation.draw(arg, alpha, batch);
    }
    public void update(int stframe, int time_step){
        if(!displayed && stframe >= first)
            displayed = animation.update(time_step);
    }
    public int getFirstFrame(){
        return first;
    }
    public Rectangle getRange(){
        return range;
    }
}
