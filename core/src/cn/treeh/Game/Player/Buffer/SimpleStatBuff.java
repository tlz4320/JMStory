package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Player.CharStat;

import java.util.HashMap;

public class SimpleStatBuff extends ActiveBuff {
    EquipStat.Id STAT;
    public SimpleStatBuff(EquipStat.Id STAT){
        this.STAT = STAT;
    }
    @Override
    void apply_to(CharStat stats, int value) {
        stats.add_buff(STAT, value);
    }
}