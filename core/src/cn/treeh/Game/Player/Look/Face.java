package cn.treeh.Game.Player.Look;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import javax.swing.*;
import java.util.HashMap;
import java.util.TreeMap;

public class Face {
    public enum Id {
        def,
        blink,
        hit,
        smile,
        troubled,
        cry,
        angry,
        bewildered,
        stunned,
        blaze,
        bowing,
        cheers,
        chu,
        dam,
        despair,
        glitter,
        hot,
        hum,
        love,
        oops,
        pain,
        shine,
        vomit,
        wink;

        public static Id byAction(int action){
            action -= 98;
            if(action < 24)
                return Id.values()[action];
            return Id.def;
        }
    }

    static class Frame{
        Texture texture;
        int delay;
        public Frame(Node src){
            texture = new Texture(src.subNode("face"));
            int[] shift = src.subNode("face/map/brow").getVector();
            shift[0] = -shift[0];
            shift[1] = -shift[1];
            texture.shift(shift);
            delay = src.subNode(delay).getInt();
            if(delay == 0)
                delay = 2500;

        }
    }
    TreeMap<Integer, Frame>[] expressions = new TreeMap[24];
    String name;
    public Face(int id){
        String str_id = "000" + id;
        Node faceNode = NXFiles.Character().subNode("Face").
                subNode(str_id + ".img");
        for(Id i : Id.values()){
            if(i == Id.def){
                if(expressions[Id.def.ordinal()] == null)
                    expressions[Id.def.ordinal()] = new TreeMap<>();
                expressions[Id.def.ordinal()].put(0,
                    new Frame(faceNode.subNode("default")));
            }
            else{
                Node expNode = faceNode.subNode(i.name());
                for(int frame = 0; ;frame++){
                    Node framenode = expNode.subNode("" + frame);
                    if(!framenode.valid())
                        break;
                    if(expressions[i.ordinal()] == null)
                        expressions[i.ordinal()] = new TreeMap<>();
                    expressions[i.ordinal()].put(frame, new Frame(framenode));
                }
            }
        }
    }
    public void draw(Id expression, int frame, DrawArg arg, SpriteBatch batch){
        Frame frame_it = expressions[expression.ordinal()] != null ?
                expressions[expression.ordinal()].get(frame) : null;
        if(frame_it != null)
            frame_it.texture.draw(arg, batch);
    }
    public int nextFrame(Id exp, int frame){
        return expressions[exp.ordinal()] != null &&
                expressions[exp.ordinal()].containsKey(frame) ? frame + 1 : 0;
    }
    public int getDelay(Id exp, int frame){
        Frame delayit = expressions[exp.ordinal()] != null ?
            expressions[exp.ordinal()].get(frame) : null;
        return delayit != null ? delayit.delay : 100;
    }
    public String getName(){
        return name;
    }
}
