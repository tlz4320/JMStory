package cn.treeh.Game.Data;

import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.BoolPair;

import java.util.AbstractMap;
import java.util.Map;
import java.util.TreeMap;

public class ItemData {
	static TreeMap<Integer, ItemData> cache = new TreeMap<>();
	boolean valid, untradable, unique, unsellable, cashitem;
	int gender, itemid, price;
	String name, desc, category;

	//first is true second is false
	BoolPair<Texture> icons = new BoolPair<>();

	static int get_item_prefix(int id)
	{
		return id / 10000;
	}
	static int get_prefix(int id)
	{
		return id / 1000000;
	}
	static String[] categorynames = new String[]
			{
				"Cap",
				"Accessory",
				"Accessory",
				"Accessory",
				"Coat",
				"Longcoat",
				"Pants",
				"Shoes",
				"Glove",
				"Shield",
				"Cape",
				"Ring",
				"Accessory",
				"Accessory",
				"Accessory"
			};

	static String get_eqcategory(int id)
	{
		int index = get_item_prefix(id) - 100;
		if (index < 15)
			return categorynames[index];
		else if (index >= 30 && index <= 70)
			return "Weapon";
		else
			return "";
	}
	int get_item_gender(int id)
	{
		int item_prefix = get_item_prefix(id);

		if ((get_prefix(id) != 1 && item_prefix != 254) || item_prefix == 119 || item_prefix == 168)
			return 2;

		int gender_digit = id / 1000 % 10;

		return (gender_digit > 1) ? 2 : gender_digit;
	}
	public static ItemData getItemData(int id){
		if(cache.containsKey(id)){
			return cache.get(id);
		} else {
			ItemData it = new ItemData(id);
			cache.put(id, it);
			return(it);
		}
	}
	private ItemData(int id){
		untradable = false;
		unique = false;
		unsellable = false;
		cashitem = false;
		gender = 0;

		Node src = null, strsrc = null;
		String strprefix = "0" + get_item_prefix(itemid);
		String strid = "0" + itemid;
		int prefix = get_prefix(itemid);

		switch (prefix)
		{
			case 1:
				category = get_eqcategory(itemid);
				src = NXFiles.Character().subNode(category).subNode(strid + ".img").subNode("info");
				strsrc = NXFiles.String().subNode("Eqp.img").subNode("Eqp").subNode(category).subNode("" + itemid);
				break;
			case 2:
				category = "Consume";
				src = NXFiles.Item().subNode("Consume").subNode(strprefix + ".img").subNode(strid).subNode("info");
				strsrc = NXFiles.String().subNode("Consume.img").subNode("" + itemid);
				break;
			case 3:
				category = "Install";
				src = NXFiles.Item().subNode("Install").subNode(strprefix + ".img").subNode(strid).subNode("info");
				strsrc = NXFiles.String().subNode("Ins.img").subNode("" + itemid);
				break;
			case 4:
				category = "Etc";
				src = NXFiles.Item().subNode("Etc").subNode(strprefix + ".img").subNode(strid).subNode("info");
				strsrc = NXFiles.String().subNode("Etc.img").subNode("Etc").subNode("" + itemid);
				break;
			case 5:
				category = "Cash";
				src = NXFiles.Item().subNode("Cash").subNode(strprefix + ".img").subNode(strid).subNode("info");
				strsrc = NXFiles.String().subNode("Cash.img").subNode("" + itemid);
				break;
		}

		if (src != null)
		{
			icons.setFalse(new Texture(src.subNode("icon")));
			icons.setTrue(new Texture(src.subNode("iconRaw")));
			price = src.subNode("price").getInt();
			untradable = src.subNode("tradeBlock").getBool();
			unique = src.subNode("only").getBool();
			unsellable = src.subNode("notSale").getBool();
			cashitem = src.subNode("cash").getBool();
			gender = get_item_gender(itemid);

			name = strsrc.subNode("name").getString();
			desc = strsrc.subNode("desc").getString();

			valid = true;
		}
		else
		{
			valid = false;
		}


	}
	boolean is_valid()
	{
		return valid;
	}

	boolean is_untradable()
	{
		return untradable;
	}

	boolean is_unique()
	{
		return unique;
	}

	boolean is_unsellable()
	{
		return unsellable;
	}

	boolean is_cashitem()
	{
		return cashitem;
	}

	int get_id()
	{
		return itemid;
	}

	int get_price()
	{
		return price;
	}

	int get_gender()
	{
		return gender;
	}

	 String get_name()
	{
		return name;
	}

	 String get_desc()
	{
		return desc;
	}

	 String get_category()
	{
		return category;
	}

	 Texture get_icon(boolean raw)
	{
		return icons.get(raw);
	}
}