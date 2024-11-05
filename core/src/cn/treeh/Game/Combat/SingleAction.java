package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.NX.Node;

public class SingleAction extends SkillAction{
    String action;
    public SingleAction(Node src){
        action = src.subNode("action").subNode("0").getString();
    }

    @Override
    public void apply(Char target, Attack.Type atype) {
        target.attack(action);
    }
}