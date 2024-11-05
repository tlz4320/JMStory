package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;
import cn.treeh.Graphics.Animation;
import cn.treeh.NX.Node;

import java.util.TreeMap;

public class BySkillLevelBullet extends SkillBullet{
    int skillid;
    TreeMap<Integer, Ball> bullets = new TreeMap<>();
    public BySkillLevelBullet(Node src, int id){
        skillid = id;
        Node sl = src.subNode("level");
        int nChild = sl.nChild();
		for (int i = 0; i < nChild; i++)
		{
            Node sub = sl.subNode(i);
			int level = 0;
            try{level = Integer.parseInt(sub.getName());
            } catch (NumberFormatException ignore) {
            }
            bullets.put(level, new Ball(sub.subNode("ball")));
		}
    }
    @Override
    public Animation get(Char user, int bulletid) {
        		int level = user.get_skilllevel(skillid);
		Ball iter = bullets.get(level);

		if (iter != null)
			return iter.animation;
		else
			return null;
    }
}