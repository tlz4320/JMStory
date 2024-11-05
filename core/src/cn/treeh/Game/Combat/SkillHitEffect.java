package cn.treeh.Game.Combat;

import cn.treeh.Graphics.Animation;
import cn.treeh.NX.Node;

public abstract class SkillHitEffect {
	public abstract void apply(AttackUser user, Mob target);
    public static class Effect
		{
			Effect(Node src)
			{
                animation = new Animation(src);
                pos = src.subNode("pos").getInt();
                z = src.subNode("z").getInt();
			}

			void apply(Mob target, boolean flip)
			{
				target.show_effect(animation, pos, z, flip);
			}

			Animation animation;
			int pos;
			int z;
		};
}