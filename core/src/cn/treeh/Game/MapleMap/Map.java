package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Physics.Physics;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.StringUtil;

public class Map {
    int mapId;
    MapTiles tiles;
    MapBackgrounds backgrounds;
    Physics physics;
    MapInfo mapInfo;
    MapPortals portals;
    public Map(int mapId){
        this.mapId = mapId;

        String strid = StringUtil.extend(mapId, 9);
        String prefix = "Map" + (mapId / 100000000);

        Node src = mapId == -1 ? NXFiles.UI().subNode("CashShopPreview.img") : NXFiles.Map().
                subNode("Map").subNode(prefix).subNode(strid + ".img");

        tiles = new MapTiles(src);
        backgrounds = new MapBackgrounds(src.subNode("back"));
        physics = new Physics(src.subNode("foothold"));
        mapInfo = new MapInfo(src, physics.getFht().getWalls(), physics.getFht().getBorders());
        portals = new MapPortals(src.subNode("portal"), mapId);
    }
}
