package cn.treeh.Game.Combat;

import cn.treeh.NX.Node;

public class SingleHitEffect  extends SkillHitEffect{
    Effect effect;
    public SingleHitEffect(Node src){
        effect = new Effect(src.subNode("hit").subNode("0"));
    }

    @Override
    public void apply(AttackUser user, Mob target) {
        effect.apply(target, user.flip);
    }
}