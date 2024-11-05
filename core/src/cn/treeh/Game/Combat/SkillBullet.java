package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.Graphics.Animation;
import cn.treeh.NX.Node;

public abstract class SkillBullet {

    abstract public Animation get(Char user, int bulletid);

    static	public class Ball
            {
                Animation animation;

                Ball(Node src)
                {
                    animation = new Animation(src);
                }

                Ball() {}
            };
}