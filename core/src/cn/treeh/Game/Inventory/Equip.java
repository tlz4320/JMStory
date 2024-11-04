package cn.treeh.Game.Inventory;

import java.util.HashMap;

public class Equip {
    public enum Potential{
        POT_NONE,
    POT_HIDDEN,
    POT_RARE,
    POT_EPIC,
    POT_UNIQUE,
    POT_LEGENDARY,
    LENGTH
    }
    int item_id, flags, slots, level, itemlevel, itemexp, vicious;
    long expiration;
    String owner;
    HashMap<EquipStat.Id, Integer> stats;
    Potential potrank;

    EquipQuality.Id quality;
    public Equip(int item_id, long expiration, String owner, int flags,
                 int slots, int level, HashMap<EquipStat.Id, Integer> stats,
                 int itemlevel, int itemexp, int vicious){
        this.item_id = item_id;
        this.expiration = expiration;
        this.owner = owner;
        this.flags = flags;
        this.slots = slots;
        this.level = level;
        this.stats = stats;
        this.itemlevel = itemlevel;
        this.itemexp = itemexp;
        this.vicious = vicious;
        potrank = Potential.POT_NONE;
        quality = EquipQuality.check_quality(item_id, level > 0, stats);
    }

    int get_item_id()
    {
        return item_id;
    }

    long get_expiration()
    {
        return expiration;
    }

    String get_owner()
    {
        return owner;
    }

    int get_flags()
    {
        return flags;
    }

    int get_slots()
    {
        return slots;
    }

    int get_level()
    {
        return level;
    }

    int get_itemlevel()
    {
        return itemlevel;
    }

    int get_stat(EquipStat.Id type)
    {
        return stats.get(type);
    }

    int get_vicious()
    {
        return vicious;
    }

    Potential get_potrank()
    {
        return potrank;
    }

    EquipQuality.Id get_quality()
    {
        return quality;
    }

}