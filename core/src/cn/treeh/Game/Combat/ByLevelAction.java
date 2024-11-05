package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.NX.Node;

import java.util.TreeMap;

public class ByLevelAction extends SkillAction{
    TreeMap<Integer, String> actions = new TreeMap<>();
    int skillid;
    public ByLevelAction(Node src, int id)
    {
        Node sl = src.subNode("level");
        int nC = sl.nChild();
        for (int i = 0; i < nC; i++)
        {
            Node sub = sl.subNode(i);
            int level = 0;
            try{level = Integer.parseInt(sub.getName());
            } catch (NumberFormatException ignore) {
            }
            actions.put(level, sub.subNode("action").getString());
        }

        skillid = id;
    }
    @Override
    public void apply(Char target, Attack.Type atype) {
        int level = target.get_skilllevel(skillid);
        String iter = actions.get(level);

        if (iter != null)
            target.attack(iter);
    }
}