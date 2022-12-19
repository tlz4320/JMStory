package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;

import java.util.TreeMap;

public class Reactor extends MapObject {

    int rid;
    int state;
    // TODO: Below
    //int stance; // ??
    // TODO: These are in the GMS client
    //boolean movable; // Snowball?
    //int questid;
    //boolean activates_by_touch;

    Node src;
    TreeMap<Integer, Animation> animations;
    boolean animation_ended;

    boolean active;
    boolean hittable;
    boolean dead;

    Animation normal;

    Reactor(int oid, int[] position) {
        super(oid, position);
    }

    public void draw(double[] realpos, float alpha, SpriteBatch batch) {
        int[] absp = phobj.get_absolute(realpos[0], realpos[1], alpha);
        absp[1] -= normal.getOrigin()[1];
        if (animation_ended) {
            // TODO: Handle 'default' animations (horntail reactor floating)
            normal.draw(new DrawArg(absp), alpha, batch);
        } else {
            animations.get(state - 1).draw(new DrawArg(absp), 1.0f, batch);
        }
    }
}
