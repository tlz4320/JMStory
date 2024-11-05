package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.NX.Node;

import java.util.Map;
import java.util.TreeMap;

public class ByLevelUseEffect extends SkillUseEffect{
    TreeMap<Integer, Effect> effects = new TreeMap<>();

    public ByLevelUseEffect(Node src){
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
            effects.put(level, new Effect(sub.subNode("effect")));
        }
    }
    @Override
    public void apply(Char target) {
        if (effects.isEmpty())
            return;
        int level = target.get_level();
        Map.Entry<Integer, Effect> e = effects.floorEntry(level);
        if(e != null)
            e.getValue().apply(target);
    }
}