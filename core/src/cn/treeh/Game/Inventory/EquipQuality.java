package cn.treeh.Game.Inventory;

import java.util.HashMap;
import java.util.Map;
import cn.treeh.Game.Data.EquipData;
public class EquipQuality {
    public 	enum Id
		{
			GREY,
			WHITE,
			ORANGE,
			BLUE,
			VIOLET,
			GOLD
		}
    public static Id check_quality(int item_id, boolean scrolled, HashMap<EquipStat.Id, Integer> stats)
	{
		EquipData data = EquipData.getEquipData(item_id);

		int delta = 0;

		for (Map.Entry<EquipStat.Id, Integer> iter : stats.entrySet())
		{
			EquipStat.Id es = iter.getKey();
			int stat = iter.getValue();
			int defstat = data.get_defstat(es);
			delta += stat - defstat;
		}

		if (delta < -5)
			return scrolled ? EquipQuality.Id.ORANGE : EquipQuality.Id.GREY;
		else if (delta < 7)
			return scrolled ? EquipQuality.Id.ORANGE : EquipQuality.Id.WHITE;
		else if (delta < 14)
			return EquipQuality.Id.BLUE;
		else if (delta < 21)
			return EquipQuality.Id.VIOLET;
		else
			return EquipQuality.Id.GOLD;
	}
}