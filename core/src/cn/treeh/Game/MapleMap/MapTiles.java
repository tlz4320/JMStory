package cn.treeh.Game.MapleMap;

import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;

public class MapTiles {
    TreeMap<Integer, LinkedList<Tile>>[] tileLayer;
    TreeMap<Integer, LinkedList<Obj>>[] objLayer;

    public MapTiles(Node node) {
        tileLayer = new TreeMap[8];
        objLayer = new TreeMap[8];
        LinkedList<Tile> tiles_tmp;
        LinkedList<Obj> objs_tmp;
        for (int layer = 0; layer < 8; layer++) {
            TreeMap<Integer, LinkedList<Tile>> tiles = new TreeMap<>();
            TreeMap<Integer, LinkedList<Obj>> objs = new TreeMap<>();
            //可能有问题
            Node src = node.subNode("" + layer);
            String tileset = src.subNode("info/tS").getString();
//            if (tileset.equals(""))
//                continue;
            tileset += ".img";
            Node tile = src.subNode("tile");
            for (int i = 0; i < tile.nChild(); i++) {
                Node tilenode = tile.subNode(i);
                Tile t = new Tile(tilenode, tileset);
                int z = t.getZ();
                tiles_tmp = tiles.getOrDefault(z, new LinkedList<>());
                tiles.put(z, tiles_tmp);
                tiles_tmp.add(t);
            }
            Node obj = src.subNode("obj");
            for (int i = 0; i < obj.nChild(); i++) {
                Node objnode = obj.subNode("" + i);
                Obj o = new Obj(objnode);
                int z = o.getZ();
                objs_tmp = objs.getOrDefault(z, new LinkedList<>());
                objs.put(z, objs_tmp);
                objs_tmp.add(o);
            }
            tileLayer[layer] = tiles;
            objLayer[layer] = objs;
        }
    }

    public void draw(int id, int[] pos, float alpha, SpriteBatch batch) {
        if (objLayer[id] != null) {
            for (Map.Entry<Integer, LinkedList<Obj>> entry : objLayer[id].entrySet()) {
                for (Obj obj : entry.getValue())
                    obj.draw(pos, alpha, batch);
            }
        }
        if (tileLayer[id] != null) {
            for (Map.Entry<Integer, LinkedList<Tile>> entry : tileLayer[id].entrySet()) {
                for (Tile tile : entry.getValue())
                    tile.draw(pos, batch);
            }
        }

    }
    public void update(){
        for(int i = 0; i < 8; i++){
            if (objLayer[i] != null) {
                for (Map.Entry<Integer, LinkedList<Obj>> entry : objLayer[i].entrySet()) {
                    for (Obj obj : entry.getValue())
                        obj.update();
                }
            }
        }
    }
}
