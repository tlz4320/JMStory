package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.NX.Node;
import cn.treeh.Util.BoolPair;

public class TwoHandedUseEffect extends SkillUseEffect{
    BoolPair<Effect> effects = new BoolPair<>();
    public TwoHandedUseEffect(Node src){
        effects.setTrue(new Effect(src.subNode("effect").subNode("0")));
        effects.setFalse(new Effect(src.subNode("effect").subNode("1")));
    }

    @Override
    public void apply(Char target) {
        effects.get(target.is_twohanded()).apply(target);
    }
}