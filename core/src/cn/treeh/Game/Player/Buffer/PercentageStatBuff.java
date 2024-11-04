package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Player.CharStat;

public class PercentageStatBuff  extends ActiveBuff {
    EquipStat.Id STAT;
    public PercentageStatBuff(EquipStat.Id STAT){
        this.STAT = STAT;
    }
    @Override
    void apply_to(CharStat stats, int value) {
        stats.add_buff(STAT, value);
    }
}