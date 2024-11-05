package cn.treeh.Game.Combat;

import cn.treeh.NX.Node;

import java.util.TreeMap;

public class BySkillLevelHitEffect extends SkillHitEffect {
    TreeMap<Integer, Effect> effects = new TreeMap<>();

    public BySkillLevelHitEffect(Node src)	{
        Node sc = src.subNode("level");
        int nChild = sc.nChild();
        for (int i = 0; i < nChild; i++)
        {
            Node sub = sc.subNode(i);
            int level = 0;
            try{
                level = Integer.parseInt(sub.getName());
            } catch (NumberFormatException ignore) {
            }
            effects.put(level, new Effect(sub.subNode("hit").subNode("0")));
        }
    }
    @Override
    public void apply(AttackUser user, Mob target) {
        Effect iter = effects.get(user.skilllevel);

		if (iter != null)
            iter.apply(target, user.flip);
    }
}