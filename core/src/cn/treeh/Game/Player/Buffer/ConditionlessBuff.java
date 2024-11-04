package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Player.CharStat;
import cn.treeh.NX.Node;

public abstract class ConditionlessBuff extends PassiveBuff{
    @Override
    boolean is_applicable(CharStat stats, Node level) {
        return true;
    }
}