package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.NX.Node;

public class SingleUseEffect extends SkillUseEffect{
    Effect effect;
    public SingleUseEffect(Node src){
        effect = new Effect(src.subNode("effect"));
    }
    @Override
    public void apply(Char target) {
        effect.apply(target);
    }
}