package cn.treeh.Graphics;

import cn.treeh.NX.Node;
import cn.treeh.Util.Configure;
import cn.treeh.Util.InterScale;
import cn.treeh.Util.StepList;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Animation {
    static class Frame {
        Texture texture;
        Rectangle bounds;
        int[] head;

        int delay;
        int[] opacities;
        int[] scales;

        public Frame(Node src) {
            texture = new Texture(src);
            bounds = new Rectangle(src);
            head = src.subNode("head").getVector();
            delay = (int) src.subNode("delay").getInt();
            if (delay == 0)
                delay = 100;
            Node a0 = src.subNode("a0");
            Node a1 = src.subNode("a1");
            if (a0.getType() == Node.Type.integer && a1.getType() == Node.Type.integer) {
                opacities = new int[]{(int) a0.getInt(), (int) a1.getInt()};
            } else if (a0.getType() == Node.Type.integer) {
                opacities = new int[]{(int) a0.getInt(), 255};
                opacities[1] -= opacities[0];
            } else if (a1.getType() == Node.Type.integer) {
                opacities = new int[]{255, (int) a1.getInt()};
                opacities[0] -= opacities[1];
            } else {
                opacities = new int[]{255, 255};
            }
            Node z0 = src.subNode("z0");
            Node z1 = src.subNode("z1");
            if (z0.getType() == Node.Type.integer && z1.getType() == Node.Type.integer) {
                scales = new int[]{(int) z0.getInt(), (int) z1.getInt()};
            } else if (z0.getType() == Node.Type.integer) {
                scales = new int[]{(int) z0.getInt(), 0};
            } else if (z1.getType() == Node.Type.integer) {
                scales = new int[]{100, (int) z1.getInt()};
            } else {
                scales = new int[]{100, 100};
            }
        }
        public Frame() {
            delay = 0;
            opacities = new int[]{0, 0};
            scales = new int[]{0, 0};
        }
        public void draw(DrawArg arg,  SpriteBatch batch)
        {
            if(texture != null)
                texture.draw(arg, batch);
        }
        public float getStepOpc(int step){
            return step * (opacities[1] - opacities[0]) / (float)delay;
        }
        public float getStepScale(int step){
            return step * (scales[1] - scales[0]) / (float)delay;
        }
        public int[] getDimension(){
            return texture.dimensions;
        }
    }


    StepList<Frame> frames;

    public Animation(Node src) {
        frames = new StepList<>();
        if (src.getType() == Node.Type.bitmap) {
            frames.add(new Frame(src));
        } else {
            TreeMap<Integer, Integer> order_set = new TreeMap<>();
            for (int i = 0; i < src.nChild(); i++) {
//                frames.add(new Frame(src.subNode("" + i)));
                Node sub = src.subNode(i);

                // in order to keep in order
                // maybe can make it order in convert?
                if (sub.getType() == Node.Type.bitmap) {
                    order_set.put(Integer.parseInt(sub.getName()), i);
                }
            }
            for (Map.Entry<Integer, Integer> i : order_set.entrySet()) {
                frames.add(new Frame(src.subNode(i.getValue())));
            }
            if(frames.isEmpty())
                frames.add(new Frame());
        }
        animated = frames.size() > 1;
        zigzag = src.subNode("zigzag").getInt() > 0;
        reset();
    }
    boolean animated;
    boolean zigzag;
    int delay;
    int framestep;
    void reset()
    {
        opacity.set(frames.get().opacities[0]);
        xy_scale.set(frames.get().scales[0]);
        delay = frames.get().delay;
        framestep = 1;
    }
    InterScale opacity = new InterScale();
    InterScale xy_scale = new InterScale();
    DrawArg drawArg;
    public void draw(DrawArg arg, float alpha,  SpriteBatch batch){
        Frame frame = frames.get(alpha);
        float inter_opc = opacity.get(alpha) / 255;
        float inter_scale = xy_scale.get(alpha) / 100;
        if(inter_opc != 1.0f && inter_scale != 1.0f) {
            drawArg = arg.add(drawArg, inter_scale, inter_scale, inter_opc);
            frame.draw(drawArg, batch);
        }
        else{
            frame.draw(arg, batch);
        }
    }
    public boolean update()
    {
        return update(Configure.TIME_STEP);
    }
    public boolean update(int step){
        Frame now = frames.get();
        opacity.add(now.getStepOpc(step));
        if(opacity.before < 0.0f){
            opacity.set(0);
        }
        else if (opacity.before > 255f){
            opacity.set(255f);
        }
        xy_scale.add(now.getStepScale(step));
        if (xy_scale.before < 0.0f)
        {
            opacity.set(0.0f);
        }

        if(step > delay){
            boolean ended;
            int goStep = 0;
            if(zigzag && frames.size() > 1) {
                if (framestep == 1 && frames.isLast()) {
                    framestep = -framestep;
                    ended = false;
                } else if (framestep == -1 && frames.isFirst()) {
                    framestep = -framestep;
                    ended = true;
                } else {
                    ended = false;
                }
                goStep = framestep;
            }
            else{
                ended = frames.isLast();
                goStep = 1;
            }
            int delta = step - delay;
            float threshold = (float)(delta) / step;
            frames.next(goStep, threshold);
            delay = frames.get().delay;
            if(delay >= delta)
                delay -= delta;
            opacity.set(frames.get().opacities[0]);
            xy_scale.set(frames.get().scales[0]);
            return ended;
        }
        else{
            frames.norm();
            delay -= step;
            return false;
        }
    }
    int getDelay(int frame_id)
    {
        return frame_id < frames.size() ? frames.get(frame_id).delay : 0;
    }
    public Frame getFrame(){
        return frames.get();
    }
    public int[] getDimension(){

        return getFrame().getDimension();
    }
}
