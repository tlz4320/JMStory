package cn.treeh.Game.Combat;

import cn.treeh.NX.Node;
import cn.treeh.Util.BoolPair;

public class TwoHandedHitEffect extends SkillHitEffect {
    BoolPair<Effect> effects = new BoolPair<>();
    public TwoHandedHitEffect(Node src){
        effects.setTrue(new Effect(src.subNode("hit").subNode("0")));
        effects.setFalse(new Effect(src.subNode("hit").subNode("1")));
    }

    @Override
    public void apply(AttackUser user, Mob target) {
        effects.get(user.secondweapon).apply(target, user.flip);
    }
}