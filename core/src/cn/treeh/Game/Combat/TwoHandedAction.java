package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.NX.Node;
import cn.treeh.Util.BoolPair;

public class TwoHandedAction extends SkillAction{
    BoolPair<String> actions = new BoolPair<>();
    public TwoHandedAction(Node src){
        actions.setFalse(src.subNode("action").subNode("0").getString());
        actions.setTrue(src.subNode("action").subNode("0").getString());
    }
    @Override
    public void apply(Char target, Attack.Type atype) {
        boolean twohanded = target.is_twohanded();
		target.attack(actions.get(twohanded));
    }
}