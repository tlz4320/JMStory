package cn.treeh.Game.Data;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Player.Look.EquipSlot;
import cn.treeh.Game.Player.MapleStat;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;

import java.util.HashMap;
import java.util.TreeMap;

public class EquipData {
	static TreeMap<Integer, EquipData> cache = new TreeMap<>();
	ItemData itemData;
	boolean cash, tradeblock;
	int slots;
	String type;
	EquipSlot.Id eqslot;
	HashMap<MapleStat.Id, Integer> reqstats;
	HashMap<EquipStat.Id, Integer> defstats;
	static 	int NON_WEAPON_TYPES = 15;
	static String[] types = new String[]
			{
					"HAT",
				"FACE ACCESSORY",
				"EYE ACCESSORY",
				"EARRINGS",
				"TOP",
				"OVERALL",
				"BOTTOM",
				"SHOES",
				"GLOVES",
				"SHIELD",
				"CAPE",
				"RING",
				"PENDANT",
				"BELT",
				"MEDAL"
			};

	static EquipSlot.Id[] equipslots = new EquipSlot.Id[]
			{
					EquipSlot.Id.HAT,
				EquipSlot.Id.FACE,
				EquipSlot.Id.EYEACC,
				EquipSlot.Id.EARACC,
				EquipSlot.Id.TOP,
				EquipSlot.Id.TOP,
				EquipSlot.Id.BOTTOM,
				EquipSlot.Id.SHOES,
				EquipSlot.Id.GLOVES,
				EquipSlot.Id.SHIELD,
				EquipSlot.Id.CAPE,
				EquipSlot.Id.RING1,
				EquipSlot.Id.PENDANT1,
				EquipSlot.Id.BELT,
				EquipSlot.Id.MEDAL
			};
	static String[] weapon_types = new String[]
	{
			"ONE-HANDED SWORD",
		"ONE-HANDED AXE",
		"ONE-HANDED MACE",
		"DAGGER",
		"", "", "",
		"WAND",
		"STAFF",
		"",
		"TWO-HANDED SWORD",
		"TWO-HANDED AXE",
		"TWO-HANDED MACE",
		"SPEAR",
		"POLEARM",
		"BOW",
		"CROSSBOW",
		"CLAW",
		"KNUCKLE",
		"GUN"
	};
	public static  EquipData getEquipData(int id){
		if(cache.containsKey(id)){
			return cache.get(id);
		} else {
			EquipData ed = new EquipData(id);
			cache.put(id, ed);
			return ed;
		}
	}
	private EquipData(int id){
		itemData = ItemData.getItemData(id);
		String strid = "0" + id;
		String category = itemData.get_category();
		Node src = NXFiles.Character().subNode(category).subNode(strid + ".img").subNode("info");

		cash = src.subNode("cash").getBool();
		tradeblock = src.subNode("tradeBlock").getBool();
		slots = src.subNode("tuc").getInt();

		reqstats.put(MapleStat.Id.LEVEL, src.subNode("reqLevel").getInt());
		reqstats.put(MapleStat.Id.JOB, src.subNode("reqJob").getInt());
		reqstats.put(MapleStat.Id.STR, src.subNode("reqSTR").getInt());
		reqstats.put(MapleStat.Id.DEX, src.subNode("reqDEX").getInt());
		reqstats.put(MapleStat.Id.INT, src.subNode("reqINT").getInt());
		reqstats.put(MapleStat.Id.LUK, src.subNode("reqLUK").getInt());
		defstats.put(EquipStat.Id.STR, src.subNode("incSTR").getInt());
		defstats.put(EquipStat.Id.DEX, src.subNode("incDEX").getInt());
		defstats.put(EquipStat.Id.INT, src.subNode("incINT").getInt());
		defstats.put(EquipStat.Id.LUK, src.subNode("incLUK").getInt());
		defstats.put(EquipStat.Id.WATK, src.subNode("incPAD").getInt());
		defstats.put(EquipStat.Id.WDEF, src.subNode("incPDD").getInt());
		defstats.put(EquipStat.Id.MAGIC, src.subNode("incMAD").getInt());
		defstats.put(EquipStat.Id.MDEF, src.subNode("incMDD").getInt());
		defstats.put(EquipStat.Id.HP, src.subNode("incMHP").getInt());
		defstats.put(EquipStat.Id.MP, src.subNode("incMMP").getInt());
		defstats.put(EquipStat.Id.ACC, src.subNode("incACC").getInt());
		defstats.put(EquipStat.Id.AVOID, src.subNode("incEVA").getInt());
		defstats.put(EquipStat.Id.HANDS, src.subNode("incHANDS").getInt());
		defstats.put(EquipStat.Id.SPEED, src.subNode("incSPEED").getInt());
		defstats.put(EquipStat.Id.JUMP, src.subNode("incJUMP").getInt());


		int WEAPON_OFFSET = NON_WEAPON_TYPES + 15;
		int WEAPON_TYPES = 20;
		int index = (id / 10000) - 100;

		if (index < NON_WEAPON_TYPES)
		{
			type = types[index];
			eqslot = equipslots[index];
		}
		else if (index >= WEAPON_OFFSET && index < WEAPON_OFFSET + WEAPON_TYPES)
		{


			int weaponindex = index - WEAPON_OFFSET;
			type = weapon_types[weaponindex];
			eqslot = EquipSlot.Id.WEAPON;
		}
		else
		{
			type = "CASH";
			eqslot = EquipSlot.Id.NONE;
		}
	}

	public boolean is_valid()
	{
		return itemData.is_valid();
	}



	boolean is_weapon()
	{
		return eqslot == EquipSlot.Id.WEAPON;
	}

	int get_reqstat(MapleStat.Id stat)
	{
		return reqstats.get(stat);
	}

	public int get_defstat(EquipStat.Id stat)
	{
		return defstats.get(stat);
	}

	public EquipSlot.Id get_eqslot()
	{
		return eqslot;
	}

	String get_type()
	{
		return type;
	}

	ItemData get_itemdata()
	{
		return itemData;
	}

}