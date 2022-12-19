package cn.treeh.Graphics;

import cn.treeh.Util.O;
import com.badlogic.gdx.graphics.Color;

import java.util.Optional;

public class DrawArg {
    public int[] store_pos;
    //for drag-able component, pos will change because of parent positon
    //therefore, store the original position
    public int[] pos;
    int[] stretch;
    float xscale;
    float yscale;
    float angle;

    Color color = new Color(1, 1, 1, 1);
    public DrawArg addPos(int[] _pos){
        this.pos[0] = store_pos[0] + _pos[0];
        this.pos[1] = store_pos[1] + _pos[1];
        return this;
    }
    public DrawArg addPos(int x, int y){
        this.pos[0] = store_pos[0] + x;
        this.pos[1] = store_pos[1] + y;
        return this;
    }
    public DrawArg subPos(int[] _pos){
        this.pos[0] = store_pos[0] - _pos[0];
        this.pos[1] = store_pos[1] - _pos[1];
        return this;
    }
    public DrawArg(int[] _pos){
        store_pos = new int[2];
        store_pos[0] = _pos[0];
        store_pos[1] = _pos[1];
        pos = _pos;
        stretch = new int[2];
        xscale = yscale = 1;
        angle = 0;
    }
    public DrawArg(int[] _pos, int[] stretch, float xscale, float yscale, float angle, Color color){
        store_pos = new int[2];
        store_pos[0] = _pos[0];
        store_pos[1] = _pos[1];
        this.pos = _pos;
        this.stretch = stretch;
        this.xscale = xscale;
        this.yscale = yscale;
        this.angle = angle;
        this.color = color;
    }
    public DrawArg(int[] _pos, int[] stretch, float xscale, float yscale, float angle, float opc){
        store_pos = new int[2];
        store_pos[0] = _pos[0];
        store_pos[1] = _pos[1];
        this.pos = _pos;
        this.stretch = stretch;
        this.xscale = xscale;
        this.yscale = yscale;
        this.angle = angle;
        color.a *= opc;
    }
    public DrawArg(int[] _pos, boolean flip){
        this(_pos, flip, 1);
    }
    public DrawArg(int[] _pos, boolean flip, float opc){
        this(_pos, new int[2], flip ? -1.0f : 1.0f, 1.0f, 0, opc);
    }
    public DrawArg(DrawArg arg, float _xscale, float _yscale, float opc){
        this(arg.pos, arg.stretch, arg.xscale + _xscale, arg.yscale + _yscale, arg.angle,
                new Color(arg.color.r, arg.color.g, arg.color.b, arg.color.a * opc));
    }
    public DrawArg(DrawArg arg){
        this(arg.pos, arg.stretch, arg.xscale, arg.yscale, arg.angle,
                new Color(arg.color.r, arg.color.g, arg.color.b, arg.color.a));
    }
    public DrawArg add(DrawArg des, int[] pos){
        if(des == null)
            des = new DrawArg(this);
        des.pos[0] = store_pos[0] + pos[0];
        des.pos[1] = store_pos[1] + pos[1];
        return des;
    }
    public DrawArg add(DrawArg des, float _xscale, float _yscale, float opc){
        if(des == null)
            des = new DrawArg(this);
        des.xscale += _xscale;
        des.yscale += _yscale;
        des.color.a *= opc;
        return des;
    }
}
