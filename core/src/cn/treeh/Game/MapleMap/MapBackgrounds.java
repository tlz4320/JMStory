package cn.treeh.Game.MapleMap;

import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import java.util.LinkedList;

public class MapBackgrounds {
    LinkedList<MapBackground> foregrounds = new LinkedList<>(),
            backgrounds = new LinkedList<>();
    boolean black;

    public MapBackgrounds(Node src) {
        int no = 0;
        Node back = src.subNode("" + no);

        while (back.nChild() > 0) {
            boolean front = back.subNode("front").getBool();

            if (front)
                foregrounds.add(new MapBackground(back));
            else
                backgrounds.add(new MapBackground(back));

            no++;
            back = src.subNode("" + no);
        }
        black = src.subNode("0/bS").getString().length() == 0;
    }

    public void draw(double[] realpos, float alpha, SpriteBatch batch) {
        if (black)
            ScreenUtils.clear(0, 0, 0, 1);
        for (MapBackground background : backgrounds)
            background.draw(realpos[0], realpos[1], alpha, batch);
        for (MapBackground background : foregrounds)
            background.draw(realpos[0], realpos[1], alpha, batch);
    }
}
