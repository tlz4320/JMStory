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
        equipdata = EquipData.get(id);
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
}
