package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Player.CharStat;
import cn.treeh.NX.Node;

public class AngelBlessingBuff extends ConditionlessBuff {

    @Override
    void apply_to(CharStat stats, Node level) {
        stats.add_value(EquipStat.Id.WATK, level.subNode("x").getInt());
        stats.add_value(EquipStat.Id.MAGIC, level.subNode("y").getInt());
        stats.add_value(EquipStat.Id.ACC, level.subNode("z").getInt());
        stats.add_value(EquipStat.Id.AVOID, level.subNode("z").getInt());
    }
}