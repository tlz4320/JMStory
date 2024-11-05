package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.Graphics.Animation;
import cn.treeh.NX.Node;

public class SingleBullet extends SkillBullet{
    SkillBullet.Ball ball;
    public SingleBullet(Node src){
        ball = new Ball(src);
    }

    @Override
    public Animation get(Char user, int bulletid) {
        return ball.animation;
    }
}