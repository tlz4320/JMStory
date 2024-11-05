package cn.treeh.Game.Combat;

import cn.treeh.NX.Node;
import cn.treeh.Util.BoolPair;

import java.util.Map;
import java.util.TreeMap;

public class ByLevelTwoHHitEffect extends SkillHitEffect {
    TreeMap<Integer, BoolPair<Effect>> effects = new TreeMap<>();
    public ByLevelTwoHHitEffect(Node src){
        Node sc = src.subNode("CharLevel");
        int nChild = sc.nChild();
		for (int i = 0; i < nChild; i++)
		{
            Node sub = sc.subNode(i);
			int level = 0;
            try{
                level = Integer.parseInt(sub.getName());
            } catch (NumberFormatException ignore) {
            }
            effects.put(level, new BoolPair<>(new Effect(sub.subNode("hit").subNode("0")),
                                              new Effect(sub.subNode("hit").subNode("1"))));
		}
    }
    @Override
    public void apply(AttackUser user, Mob target) {
        if (effects.isEmpty())
			return;
        Map.Entry<Integer, BoolPair<Effect>> e = effects.floorEntry(user.level);
        if(e == null){
            e = effects.firstEntry();
        }
		e.getValue().get(user.secondweapon).apply(target, user.flip);
    }
}