package cn.treeh.IO.Net;

import cn.treeh.Game.Player.MapleStat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class StatsEntry {
    public String name;
    public boolean female;
    public LinkedList<Long> petids;
    public HashMap<MapleStat.Id, Integer> stats;
    public long exp;
    public int mapid;
    public int portal;
    public Map.Entry<Integer, Integer> rank;
    public Map.Entry<Integer, Integer> jobrank;
}