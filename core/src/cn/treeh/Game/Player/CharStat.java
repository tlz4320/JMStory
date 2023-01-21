package cn.treeh.Game.Player;

import cn.treeh.Game.Inventory.Weapon;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CharStat {
    String name;
    LinkedList<Integer> petids;
    Job job;
    long exp;
    int mapid;
    int portal;
    Map.Entry<Integer, Integer> rank;
    Map.Entry<Integer, Integer> jobrank;
    HashMap<MapleStat.Id, Integer> basestats = new HashMap<>();
    HashMap<EquipStat.Id, Integer> totalstats = new HashMap<>();
    HashMap<EquipStat.Id, Integer> buffdeltas = new HashMap<>();
    HashMap<EquipStat.Id, Float> percentages = new HashMap<>();
    int maxdamage;
    int mindamage;
    int honor;
    int attackspeed;
    int projectilerange;
    Weapon.Type weapontype;
    float mastery;
    float critical;
    float mincrit;
    float maxcrit;
    float damagepercent;
    float bossdmg;
    float ignoredef;
    float stance;
    float resiststatus;
    float reducedamage;
    boolean female;
    public int get_total(EquipStat.Id stat){
        return totalstats.getOrDefault(stat, 0);
    }
}
