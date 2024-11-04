package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Player.CharStat;
import cn.treeh.Game.Player.MapleStat;
import cn.treeh.NX.Node;

public class BerserkBuff extends PassiveBuff{

    @Override
    boolean is_applicable(CharStat stats, Node level) {
        float hp_percent = level.subNode("x").getInt() / 100f;
        int hp_threshold = (int)(stats.get_total(EquipStat.Id.HP) * hp_percent);
		int hp_current = stats.get_stat(MapleStat.Id.HP);

		return hp_current <= hp_threshold;
    }

    @Override
    void apply_to(CharStat stats, Node level) {
        float damagepercent = level.subNode("damage").getInt() / 100f;
		stats.set_damagepercent(damagepercent);
    }
}