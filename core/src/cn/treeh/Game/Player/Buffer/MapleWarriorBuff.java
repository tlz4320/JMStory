package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Player.CharStat;

public class MapleWarriorBuff extends ActiveBuff{

    @Override
    void apply_to(CharStat stats, int value) {
        stats.add_percent(EquipStat.Id.STR, value / 100f);
		stats.add_percent(EquipStat.Id.DEX, value / 100f);
		stats.add_percent(EquipStat.Id.INT, value / 100f);
		stats.add_percent(EquipStat.Id.LUK, value / 100f);
    }
}