package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.Animation;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;

import java.util.TreeMap;

public class MapDrops {
    enum MesoIcon
    {
        BRONZE,
        GOLD,
        BUNDLE,
        BAG,
        NUM_ICONS
    };
    TreeMap<Integer, Drop>[] drops;
    Animation[] mesoicons = new Animation[MesoIcon.NUM_ICONS.ordinal()];

    public void init() {
        Node src = NXFiles.Item().subNode("Special/0900.img");

        mesoicons[MesoIcon.BRONZE.ordinal()] = new Animation(src.subNode("09000000/iconRaw"));
        mesoicons[MesoIcon.GOLD.ordinal()] = new Animation(src.subNode("09000001/iconRaw"));
        mesoicons[MesoIcon.BUNDLE.ordinal()] = new Animation(src.subNode("09000002/iconRaw"));
        mesoicons[MesoIcon.BAG.ordinal()] = new Animation(src.subNode("09000003/iconRaw"));
    }
}
