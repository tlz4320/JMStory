package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Player.CharStat;

public class StanceBuff extends ActiveBuff{

    @Override
    void apply_to(CharStat stats, int value) {
        stats.set_stance(value / 100f);
    }
}