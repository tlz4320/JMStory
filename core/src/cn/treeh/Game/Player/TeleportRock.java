package cn.treeh.Game.Player;

import java.util.LinkedList;

public class TeleportRock {
    LinkedList<Integer> locations = new LinkedList<>();
    LinkedList<Integer> viplocations = new LinkedList<>();
    void addlocation(int mapid)
	{
		locations.addLast(mapid);
	}

	void addviplocation(int mapid)
	{
		viplocations.addLast(mapid);
	}
}