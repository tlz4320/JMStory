package cn.treeh.Game.Data;

import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.NX.Audio;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;

import java.util.TreeMap;

public class WeaponData {
    static TreeMap<Integer, WeaponData> cache = new TreeMap<>();

    public static WeaponData get(int itemid) {
        if (cache.containsKey(itemid))
            return cache.get(itemid);
        WeaponData data = new WeaponData(itemid);
        cache.put(itemid, data);
        return data;
    }
    WeaponData(int id){
        equipdata = EquipData.getEquipData(id);
        int prefix = id / 10000;
        type = Weapon.byId(prefix);
        twohanded = prefix == Weapon.Type.STAFF.id || (prefix >= Weapon.Type.SWORD_2H.id && prefix <= Weapon.Type.POLEARM.id) || prefix == Weapon.Type.CROSSBOW.id;

        Node src = NXFiles.Character().subNode("Weapon")
                .subNode("0" + id + ".img").subNode("info");

        attackspeed =src.subNode("attackSpeed").getInt();
        attack = src.subNode("attack").getInt();

        Node soundsrc = NXFiles.Audio().subNode("Weapon.img").subNode(src.subNode("sfx").getString());

        boolean twosounds = soundsrc.subNode("Attack2").getType() == Node.Type.audio;

        if (twosounds)
        {
            sound_false = soundsrc.subNode("Attack").getAudio();
            sound_true = soundsrc.subNode("Attack2").getAudio();
        }
        else
        {
            sound_false = soundsrc.subNode("Attack").getAudio();
            sound_true = soundsrc.subNode("Attack").getAudio();
        }

        afterimage = src.subNode("afterImage").getString();
    }

	EquipData equipdata;

    Weapon.Type type;
    boolean twohanded;
    int attackspeed;
    int attack;
    Audio sound_true;
    Audio sound_false;
    String afterimage;

    public boolean isTwohanded(){
        return twohanded;
    }

    boolean is_valid()
	{
		return equipdata.is_valid();
	}

	int get_speed()
	{
		return attackspeed;
	}

	public int get_attack()
	{
		return attack;
	}

	String getspeedstring()
	{
		switch (attackspeed)
		{
		case 1:
			return "FAST (1)";
		case 2:
			return "FAST (2)";
		case 3:
			return "FAST (3)";
		case 4:
			return "FAST (4)";
		case 5:
			return "NORMAL (5)";
		case 6:
			return "NORMAL (6)";
		case 7:
			return "SLOW (7)";
		case 8:
			return "SLOW (8)";
		case 9:
			return "SLOW (9)";
		default:
			return "";
		}
	}

	int get_attackdelay()
	{
		if (type == Weapon.Type.NONE)
			return 0;
		else
			return 50 - 25 / attackspeed;
	}

	Weapon.Type get_type()
	{
		return type;
	}

	public Audio get_usesound(boolean degenerate)
	{
		return degenerate ? sound_true : sound_false;
	}

	String get_afterimage()
	{
		return afterimage;
	}

	 EquipData get_equipdata()
	{
		return equipdata;
	}

}
