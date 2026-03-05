package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.StringUtil;
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

    public Reactor(int oid, int r, int s, int[] position) {
        super(oid, position);
        rid = r;
        state = s;
        String strid = StringUtil.extend(rid, 7);
        src = NXFiles.Reactor().subNode(strid + ".img");

        normal = new Animation(src.subNode(0));
		animation_ended = true;
		dead = false;
		hittable = false;

            Node sub = src.subNode(0).subNode("event");
            if (sub.valid())
				if (src.subNode(0).subNode("type").getInt() == 0)
					hittable = true;
    }
    DrawArg arg = new DrawArg();
    public void draw(double viewx, double viewy, float alpha, SpriteBatch batch) {
        int[] absp = phobj.get_absolute(viewx, viewy, alpha);
        absp[1] -= normal.getOrigin()[1];
        if (animation_ended) {
            arg.pos = arg.store_pos = absp;
            // TODO: Handle 'default' animations (horntail reactor floating)
            normal.draw(arg, alpha, batch);
        } else {
            animations.get(state - 1).draw(new DrawArg(absp), 1.0f, batch);
        }
    }
}
