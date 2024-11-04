package cn.treeh.Game.Inventory;

import cn.treeh.Game.Data.EquipData;
import cn.treeh.Game.Player.Look.EquipSlot;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class Inventory {
	public class Slot
				{
					int unique_id;
					int item_id;
					int count;
					boolean cash;
					public Slot(int uid, int itemid, int count, boolean cash){
						this.unique_id = uid;
						this.item_id = itemid;
						this.count = count;
						this.cash = cash;
					}
				};
	enum Movement{
		MOVE_NONE(-1),
	MOVE_INTERNAL(0),
	MOVE_UNEQUIP(1),
	MOVE_EQUIP(2);
		public int id;
		Movement(int id){
			this.id = id;
		}
	static Movement byId(int id){
			switch (id){
				case 0:
					return MOVE_INTERNAL;
					case 1:
						return MOVE_UNEQUIP;
						case 2:
							return MOVE_EQUIP;
							default:
								return MOVE_NONE;
			}
		}
	}

	enum Modification
					{
						ADD,
						CHANGECOUNT,
						SWAP,
						REMOVE,
						ADDCOUNT
					};

	HashMap<InventoryType.Id, TreeMap<Integer, Slot>> inventories = new HashMap<>();
	TreeMap<Integer, Item> items;
	TreeMap<Integer, Equip> equips;
	TreeMap<Integer, Pet> pets;
	int running_uid;

	HashMap<EquipStat.Id, Integer> totalstats = new HashMap<>();
	HashMap<InventoryType.Id, Integer> slotmaxima = new HashMap<>();
	long meso;
	int bulletslot;

	Inventory(){
		bulletslot = 0;
		meso = 0;
		running_uid = 0;
		for(InventoryType.Id id : InventoryType.Id.values()){
			inventories.put(id, new TreeMap<>());
		}
		slotmaxima.put(InventoryType.Id.EQUIPPED, EquipSlot.Id.LENGTH.ordinal());
	}
	void recalc_stats(Weapon.Type type)
	{
		totalstats.clear();

		for (Map.Entry<Integer, Slot> iter : inventories.get(InventoryType.Id.EQUIPPED).entrySet())
		{
			Equip equip = equips.get(iter.getValue().unique_id);

			if (equip != null)
			{
				for (Map.Entry<EquipStat.Id, Integer> stat_iter : totalstats.entrySet())
					stat_iter.setValue(stat_iter.getValue() + equip.get_stat(stat_iter.getKey()));
			}
		}

		int prefix;

		switch (type)
		{
			case BOW:
				prefix = 2060;
				break;
				case CROSSBOW:
					prefix = 2061;
					break;
					case CLAW:
						prefix = 2070;
						break;
						case GUN:
							prefix = 2330;
							break;
							default:
								prefix = 0;
								break;
		}
		//计算出第一个在使用的箭矢或者子弹
		bulletslot = 0;

		if (prefix != 0)
		{
			for (Map.Entry<Integer, Slot> iter : inventories.get(InventoryType.Id.USE).entrySet())
			{
				Slot slot = iter.getValue();

				if (slot.count != 0 && slot.item_id / 1000 == prefix)
				{
					bulletslot = iter.getKey();
					break;
				}
			}
		}
		int bulletid = get_bulletid();
		if (bulletid != 0)
			totalstats.put(EquipStat.Id.WATK, totalstats.getOrDefault(EquipStat.Id.WATK, 0) +
											  BulletData.getBulletData(bulletid).get_watk());
	}

	void set_meso(long m){
		meso = m;
	}

	void set_slotmax(InventoryType.Id type, int slotmax)
	{
		slotmaxima.put(type, slotmax);
	}

	void add_item(InventoryType.Id invtype, int slot, int item_id, boolean cash, long expire, int count, String owner, int flags)
	{
		items.put(add_slot(invtype, slot, item_id, count, cash),
				  new Item(item_id, expire, owner, flags)
		);
	}

	void add_pet(InventoryType.Id invtype, int slot, int item_id, boolean cash, long expire, String name, int level, int closeness, int fullness)
	{
		pets.put(add_slot(invtype, slot, item_id, 1, cash),
				 new Pet(item_id, expire, name, level, closeness, fullness)
		);
	}

	void add_equip(InventoryType.Id invtype, int slot, int item_id, boolean cash, long expire,
				   int slots, int level, HashMap<EquipStat.Id, Integer> stats, String owner, int flag, int ilevel, int iexp, int vicious)
	{
		equips.put(add_slot(invtype, slot, item_id, 1, cash),
				   new Equip(item_id, expire, owner, flag, slots, level, stats, ilevel, iexp, vicious)
		);
	}

	void remove(InventoryType.Id type, int slot)
	{
		Slot iter = inventories.get(type).get(slot);

		if(iter == null){
			return;
		}

		int unique_id = iter.unique_id;
		inventories.get(type).remove(slot);

		switch (type)
		{
			case EQUIPPED:
				case EQUIP:
					equips.remove(unique_id);
					break;
					case CASH:
						items.remove(unique_id);
						pets.remove(unique_id);
						break;
						default:
							items.remove(unique_id);
							break;
		}
	}
	//把两个位置的道具互换
	//TODO:	按道理说  相同道具应该合并，但是这里似乎没有这个逻辑，最后再回来检查
	void swap(InventoryType.Id firsttype, int firstslot, InventoryType.Id secondtype, int secondslot)
	{
		Slot first = inventories.get(firsttype).get(firstslot);
		Slot second = inventories.get(secondtype).get(secondslot);
		inventories.get(firsttype).put(firstslot, second);
		inventories.get(secondtype).put(secondslot, first);

		if (second == null)
			remove(firsttype, firstslot);

		if (first == null)
			remove(secondtype, secondslot);
	}

	int add_slot(InventoryType.Id type, int slot, int item_id, int count, boolean cash)
	{
		running_uid++;
		if(!inventories.containsKey(type)){
			inventories.put(type, new TreeMap<>());
		}
		inventories.get(type).put(slot, new Slot(running_uid, item_id, count, cash));

		return running_uid;
	}

	void change_count(InventoryType.Id type, int slot, int count)
	{
		Slot iter = inventories.get(type).get(slot);

		if (iter != null)
			iter.count = count;
	}

	void modify(InventoryType.Id type, int slot, Modification mode, int arg, Movement move)
	{
		if (slot < 0)
		{
			slot = -slot;
			type = InventoryType.Id.EQUIPPED;
		}

		arg = (arg < 0) ? -arg : arg;

		switch (mode)
		{
			case CHANGECOUNT:
				change_count(type, slot, arg);
				break;
				case SWAP:
					switch (move)
					{
						case MOVE_INTERNAL:
							swap(type, slot, type, arg);
							break;
							case MOVE_UNEQUIP:
								swap(InventoryType.Id.EQUIPPED, slot, InventoryType.Id.EQUIP, arg);
								break;
								case MOVE_EQUIP:
									swap(InventoryType.Id.EQUIP, slot, InventoryType.Id.EQUIPPED, arg);
									break;
					}

			break;
					case REMOVE:
						remove(type, slot);
						break;
		}
	}

	int get_slotmax(InventoryType.Id type)
	{
		if (type == InventoryType.Id.DEC)
			return slotmaxima.get(InventoryType.Id.CASH);

		return slotmaxima.get(type);
	}

	int get_stat(EquipStat.Id type)
	{
		return totalstats.get(type);
	}

	long get_meso()
	{
		return meso;
	}

	boolean has_projectile()
	{
		return bulletslot > 0;
	}

	boolean has_equipped(EquipSlot.Id slot)
	{
		return inventories.get(InventoryType.Id.EQUIPPED).get(slot).count > 0;
	}

	int get_bulletslot()
	{
		return bulletslot;
	}

	int get_bulletcount()
	{
		return get_item_count(InventoryType.Id.USE, bulletslot);
	}

	int get_bulletid()
	{
		return get_item_id(InventoryType.Id.USE, bulletslot);
	}

	EquipSlot.Id find_equipslot(int itemid)
	{
		EquipData cloth = EquipData.getEquipData(itemid);

		if (!cloth.is_valid())
			return EquipSlot.Id.NONE;

		EquipSlot.Id eqslot = cloth.get_eqslot();

		if (eqslot == EquipSlot.Id.RING1)
		{
			if (!has_equipped(EquipSlot.Id.RING2))
				return EquipSlot.Id.RING2;

			if (!has_equipped(EquipSlot.Id.RING3))
				return EquipSlot.Id.RING3;

			if (!has_equipped(EquipSlot.Id.RING4))
				return EquipSlot.Id.RING4;

			return EquipSlot.Id.RING1;
		}
		else
		{
			return eqslot;
		}
	}

	int find_free_slot(InventoryType.Id type)
	{
		int counter = 1;

		for (Map.Entry<Integer, Slot> iter: inventories.get(type).entrySet())
		{
			if (iter.getKey() != counter)
				return counter;

			counter++;
		}

		return counter <= slotmaxima.get(type) ? counter : 0;
	}

	int find_item(InventoryType.Id type, int itemid)
	{
		for (Map.Entry<Integer, Slot> iter : inventories.get(type).entrySet())
			if (iter.getValue().item_id == itemid)
				return iter.getKey();

		return 0;
	}

	int get_item_count(InventoryType.Id type, int slot)
	{
		Slot iter = inventories.get(type).get(slot);

		return iter == null ? 0 : iter.count;
	}

	int get_total_item_count(int itemid)
	{
		InventoryType.Id type = InventoryType.by_item_id(itemid);

		int total_count = 0;

		for (Map.Entry<Integer, Slot> iter : inventories.get(type).entrySet())
			if (iter.getValue().item_id == itemid)
				total_count += iter.getValue().count;

		return total_count;
	}

	int get_item_id(InventoryType.Id type, int slot)
	{
		Slot iter = inventories.get(type).get(slot);
		return iter == null ? 0 : iter.item_id;
	}

	Equip get_equip(InventoryType.Id type, int slot)
	{
		if (type != InventoryType.Id.EQUIPPED && type != InventoryType.Id.EQUIP)
			return null;

		Slot slot_iter = inventories.get(type).get(slot);
		if(slot_iter == null){
			return null;
		}
		return equips.get(slot_iter.unique_id);
	}

	Movement movementbyvalue(int value)
	{
		if (value >= Movement.MOVE_INTERNAL.id && value <= Movement.MOVE_EQUIP.id)
			return Movement.byId(value);
		return Inventory.Movement.MOVE_NONE;
	}
}