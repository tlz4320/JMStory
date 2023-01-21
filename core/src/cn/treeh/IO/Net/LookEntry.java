package cn.treeh.IO.Net;

import java.util.LinkedList;
import java.util.TreeMap;

public class LookEntry {
    public boolean female;
    public int skin;
    public int face;
    public int hair;
    public TreeMap<Integer, Integer> equips;
    public TreeMap<Integer, Integer> equip_masks;
    public LinkedList<Integer> pets;

    public LookEntry(int _s, int _f, int _h){
        female = false;
        skin = _s;
        face = _f;
        hair = _h;
        equips = new TreeMap<>();
        equip_masks = new TreeMap<>();
        pets = new LinkedList<>();
    }

}
