package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Player.CharStat;
import cn.treeh.NX.Node;

public abstract class PassiveBuff {
    abstract boolean is_applicable(CharStat stats, Node level);
    abstract void apply_to(CharStat stats, Node level);
}