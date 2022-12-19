package cn.treeh.Game.MapleMap;

import cn.treeh.Audio.BgmPlayer;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Player.Player;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.StringUtil;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Map {
    int mapId;
    MapTiles tiles;
    MapBackgrounds backgrounds;
    Physics physics;
    MapInfo mapInfo;
    MapPortals portals;

    MapReactors reactors;
    MapNPCs npcs;
    MapChars chars;
    MapMobs mobs;
    MapDrops drops;
    MapEffect effect;
    public Map(){
        drops = new MapDrops();
    }
    public void init(){
        drops.init();
    }
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
    public int[] getPortalByID(int id){
        return portals.getPortalByID(id);
    }
    public void BGM(){
        BgmPlayer.play(mapInfo.getBgm());
    }
    public int[] getWalls(){
        return mapInfo.mapwalls;
    }
    public int[] getBorders(){
        return mapInfo.mapborders;
    }
    public void drawBackground(double[] realpos, float alpha, SpriteBatch batch){
        backgrounds.draw(realpos, alpha, batch);
    }
    public void drawLayer(int layer, int[] pos, double[] realpos, float alpha, SpriteBatch batch){
        tiles.draw(layer, pos, alpha, batch);
//        reactors.draw(layer, realpos, alpha, batch);
//        npcs.draw(layer viewx, viewy, alpha);
//        mobs.draw(layer, viewx, viewy, alpha);
//        chars.draw(layer, viewx, viewy, alpha);
    }
    public int[]get_y_below(int[] pos){
        return physics.get_y_below(pos);
    }
    public void update(Player player){
//        tiles.update();
    }
}
