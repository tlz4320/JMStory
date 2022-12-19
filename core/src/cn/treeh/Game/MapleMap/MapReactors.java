package cn.treeh.Game.MapleMap;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Map;
import java.util.TreeMap;

public class MapReactors {
    TreeMap<Integer, Reactor>[] layers;
    public MapReactors(){
        layers = new TreeMap[8];
    }
    public void draw(int layer, double[] pos, float alpha, SpriteBatch batch){
        if(layers[layer] != null){
            for (Map.Entry<Integer, Reactor> entry : layers[layer].entrySet()){
                entry.getValue().draw(pos, alpha, batch);
//                if (mmo && mmo->is_active())
//                    mmo->draw(viewx, viewy, alpha);
            }
        }
    }
}
