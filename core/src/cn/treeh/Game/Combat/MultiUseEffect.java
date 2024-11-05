package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.NX.Node;

import java.util.LinkedList;

public class MultiUseEffect extends SkillUseEffect {
    LinkedList<Effect> effects = new LinkedList<>();
    public MultiUseEffect(Node src){
        int no = -1;
        Node sub = src.subNode("effect");
        while(sub.valid()){
            effects.add(new Effect(sub));
            no++;
            sub = src.subNode("effect" + no);
        }
    }
    @Override
    public void apply(Char target) {
        for (Effect effect : effects)
			effect.apply(target);
    }
}