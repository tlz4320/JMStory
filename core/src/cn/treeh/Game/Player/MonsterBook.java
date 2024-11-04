package cn.treeh.Game.Player;

import java.util.TreeMap;

public class MonsterBook {
    int cover;
	static TreeMap<Integer, Integer> cards = new TreeMap<>();
    public MonsterBook()
	{
		cover = 0;
	}

	public void set_cover(int cov)
	{
		cover = cov;
	}

    public void add_card(int card, int level)
	{
        cards.put(card, level);
	}
}