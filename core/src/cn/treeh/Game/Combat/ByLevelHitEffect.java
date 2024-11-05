package cn.treeh.Game.Combat;

import cn.treeh.NX.Node;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class ByLevelHitEffect  extends SkillHitEffect{
    TreeMap<Integer, Effect> effects = new TreeMap<>();
    public ByLevelHitEffect(Node src)
	{
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
            effects.put(level, new Effect(sub.subNode("hit").subNode("0")));
		}
	}

    @Override
    public void apply(AttackUser user, Mob target) {
        if (effects.isEmpty())
			return;
        Map.Entry<Integer, Effect> e = effects.floorEntry(user.level);
        if(e == null){
            e = effects.firstEntry();
        }
		e.getValue().apply(target, user.flip);
    }
}