package cn.treeh.Game.MapleMap;

import cn.treeh.NX.Node;

public class Ladder
{
    Ladder(Node src){
        x = src.subNode("x").getInt();
        y1 = src.subNode("y1").getInt();
        y2 = src.subNode("y2").getInt();
        ladder = src.subNode("l").getBool();
    }

    public boolean is_ladder(){
        return ladder;
    }
    public boolean inrange(int[] position, boolean upwards){
        int y = upwards ? position[1] - 5 :
                position[1] + 5;

        return Math.abs(position[0] - x) <= 10 && (y1 <= y && y2 >= y);
    }
    public boolean felloff(int y, boolean downwards){
        int dy = downwards ? y + 5 : y - 5;

        return dy > y2 || y + 5 < y1;
    }
    public int get_x(){
        return x;
    }
    int x;
    int y1;
    int y2;
    boolean ladder;
}