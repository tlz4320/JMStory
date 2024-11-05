package cn.treeh.Game.Player.Job;

import java.util.TreeMap;

public class SkillEntry {
    int level;
    int masterlevel;
    long expiration;
    public SkillEntry(int _l, int _m, long _e){
        level = _l;
        masterlevel = _m;
        expiration = _e;
    }
}