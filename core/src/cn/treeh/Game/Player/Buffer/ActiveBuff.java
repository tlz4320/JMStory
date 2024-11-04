package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Player.CharStat;

public abstract class ActiveBuff {
    abstract void apply_to(CharStat stats, int value);
}