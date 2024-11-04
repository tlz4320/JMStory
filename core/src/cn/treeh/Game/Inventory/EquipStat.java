package cn.treeh.Game.Inventory;

public class EquipStat {
    public enum Id
		{
			STR,
			DEX,
			INT,
			LUK,
			HP,
			MP,
			WATK,
			MAGIC,
			WDEF,
			MDEF,
			ACC,
			AVOID,
			HANDS,
			SPEED,
			JUMP,
			LENGTH
		}
	public static Id byId(int value){
        return Id.values()[value];
    }
	static String[] names = new String[]{ "STR", "DEX","INT","LUK",	"MaxHP","MaxMP","Attack Power",
	"Magic Attack","Defense","MAGIC DEFENSE","ACCURACY","AVOID","HANDS","Speed","Jump"
	};
}