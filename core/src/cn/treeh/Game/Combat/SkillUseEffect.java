package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.Graphics.Animation;
import cn.treeh.NX.Node;

public abstract class SkillUseEffect {
    public static class Effect
		{
			Effect(Node src)
			{
                animation = new Animation(src);
                z = src.subNode("z").getInt();
			}

			public void apply(Char target)
			{
				target.showAttackEffect(animation, z);
			}

			Animation animation;
			int z;
		};
    public abstract void apply(Char target);
}