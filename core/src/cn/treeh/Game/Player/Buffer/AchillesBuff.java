package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Player.CharStat;
import cn.treeh.NX.Node;

public class AchillesBuff extends ConditionlessBuff{

    @Override
    void apply_to(CharStat stats, Node level) {
        float reducedamage = level.subNode("x").getInt() / 1000f;
		stats.set_reducedamage(reducedamage);
    }
}