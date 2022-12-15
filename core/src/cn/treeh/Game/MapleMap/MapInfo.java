package cn.treeh.Game.MapleMap;

import cn.treeh.NX.Node;

import java.util.LinkedList;

public class MapInfo {
    int[] mapwalls, mapborders;
    boolean cloud;
    String bgm;
    String mapdesc;
    String mapname;
    String streetname;
    String mapmark;
    boolean swim;
    boolean town;
    boolean hideminimap;
    int fieldlimit;
    LinkedList<Seat> seats;
    LinkedList<Ladder> ladders;
    public MapInfo(Node src, int[] w, int[] b){
        seats = new LinkedList<>();
        ladders = new LinkedList<>();
        Node info = src.subNode("info");
        Node vrLeft = info.subNode("VRLeft");
        if (vrLeft.getType() == Node.Type.integer)
        {
            mapwalls = new int[]{vrLeft.getInt(), info.subNode("VRRight").getInt()};
            mapborders = new int[]{info.subNode("VRTop").getInt(), info.subNode("VRBottom").getInt()};
        }
		else
        {
            mapwalls = w;
            mapborders = b;
        }

        String bgmPath = info.subNode("bgm").getString("");
        int split = bgmPath.indexOf('/');
        bgm = bgmPath.substring(0, split) + ".img/" + bgmPath.substring(split + 1);

        cloud = info.subNode("cloud").getBool();
        fieldlimit = info.subNode("fieldLimit").getInt();
        hideminimap = info.subNode("hideMinimap").getBool();
        mapmark = info.subNode("mapMark").getString("");
        swim = info.subNode("swim").getBool();
        town = info.subNode("town").getBool();
        Node seat = src.subNode("seat");
        for(int i = 0; i < seat.nChild(); i++)
            seats.add(new Seat(seat.subNode(i)));
        Node ladderRope = src.subNode("ladderRope");
        for(int i = 0; i < ladderRope.nChild(); i++)
            ladders.add(new Ladder(ladderRope.subNode(i)));
        
    }


    class Seat{
        
        Seat(Node source){
            pos = source.getVector();
        }

        boolean inrange(int[] position){
            return Math.abs(pos[0] - position[0]) <= 10 &&
                    Math.abs(pos[1] - position[1]) <= 10;
        }
        int[] getpos(){
            return pos;
        }
        int[] pos;
    };

    class Ladder
    {
        Ladder(Node src){
            x = src.subNode("x").getInt();
            y1 = src.subNode("y1").getInt();
            y2 = src.subNode("y2").getInt();
            ladder = src.subNode("l").getBool();
        }

        boolean is_ladder(){
            return ladder;
        }
        boolean inrange(int[] position, boolean upwards){
            int y = upwards ? position[1] - 5 :
                    position[1] + 5;

            return Math.abs(position[0] - x) <= 10 && (y1 <= y && y2 >= y);
        }
        boolean felloff(int y, boolean downwards){
            int dy = downwards ? y + 5 : y - 5;

            return dy > y2 || y + 5 < y1;
        }
        int get_x(){
            return x;
        }
        int x;
        int y1;
        int y2;
        boolean ladder;
    };
}
