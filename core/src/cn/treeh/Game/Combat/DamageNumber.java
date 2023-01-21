package cn.treeh.Game.Combat;

import cn.treeh.Game.Physics.MovingObject;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.UI.Component.Charset;
import cn.treeh.Util.Configure;
import cn.treeh.Util.InterScale;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Objects;

public class DamageNumber {

    public enum Type
    {
        NORMAL,
        CRITICAL,
        TOPLAYER
    };
    int reverse(int damage){
        rev_num = 0;
        int res = 0;
        while(damage > 0){
            rev_num = rev_num * 10 + (damage % 10);
            damage = damage / 10;
            res++;
        }
        return res;
    }
    DamageNumber(Type t, int damage, int starty, int x)
    {
        type = t;
        num = damage;
        count = reverse(num);
        int remain = 0;
        if (damage > 0)
        {
            miss = false;

            remain = rev_num / 10;

            multiple = damage > 9;

            int total = getadvance(rev_num % 10, true);
            int advance = 0;
            for (int i = 1; i < count; i++)
            {
                int c = remain % 10;
                remain = remain / 10;
                if (i < count-1)
                {
                    advance = (getadvance(c, false) + getadvance(remain % 10, false)) / 2;
                }
                else
                {
                    advance = getadvance(c, false);
                }

                total += advance;
            }

            shift = total / 2;
        }
        else
        {
            shift = charsets[type.ordinal()][1].getw('M') / 2;
            miss = true;
        }
        moveobj = new MovingObject();
        opacity = new InterScale();
        opacity.set(1.5f);
        moveobj.set_x(x);
        moveobj.set_y(starty);
        moveobj.vspeed = -0.25;
    }

    static int FADE_TIME = 500;

    Type type;
    boolean miss;
    boolean multiple;
    int count;
    int num;
    int rev_num;
    int shift;
    MovingObject moveobj;
   InterScale opacity;

    static Charset[][] charsets = new Charset[3][2];
    int[] advances =
    {
        24, 20, 22, 22, 24, 23, 24, 22, 24, 24
    };
    int getadvance(int index, boolean first)
    {

        if (index < 10)
        {
            int advance = advances[index];

            if (Objects.requireNonNull(type) == Type.CRITICAL) {
                if (first)
                    advance += 8;
                else
                    advance += 4;
            } else {
                if (first)
                    advance += 2;
            }

            return advance;
        }
        else
        {
            return 0;
        }
    }
    public void draw(double viewx, double viewy, float alpha, SpriteBatch batch)
    {
        int[] absolute = moveobj.get_absolute(viewx, viewy, alpha);
        int[] position = new int[]{absolute[0], absolute[1] - shift};
        float interopc = opacity.get(alpha);

        if (miss)
        {
            charsets[type.ordinal()][1].draw('M',new DrawArg(position, interopc), batch);
        }
        else
        {
            DrawArg arg = new DrawArg( position, interopc );
            charsets[type.ordinal()][0].draw(rev_num % 10, arg, batch);

            if (multiple)
            {
                int first_advance = getadvance(rev_num % 10, true);
                int x_shift = first_advance;
                int remain = rev_num / 10;
                for (int i = 1; i < count; i++)
                {
                    int c = remain % 10;
                    remain = remain / 10;
                    arg.addPos(x_shift,  ((i % 2) != 0) ? -2 : 2);
                    charsets[type.ordinal()][1].draw(c, arg, batch);

                    int advance;

                    if (i < count - 1)
                    {

                        int c_advance = getadvance(c, false);
                        int n_advance = getadvance(remain % 10, false);
                        advance = (c_advance + n_advance) / 2;
                    }
                    else
                    {
                        advance = getadvance(c, false);
                    }

                    x_shift += advance;
                }
            }
        }
    }
    public boolean update()
    {
        moveobj.move();
        float FADE_STEP = (float) Configure.TIME_STEP / FADE_TIME;
        opacity.add(-FADE_STEP);
        return opacity.last() <= 0.0f;
    }
    public  void init(){
        Node BasicEff = NXFiles.Effect().subNode("BasicEff.img");

        charsets[Type.NORMAL.ordinal()][0] = new Charset(BasicEff.subNode("NoRed1"), 
                Charset.Alignment.LEFT);
        charsets[Type.NORMAL.ordinal()][1] = new Charset(BasicEff.subNode("NoRed0"), 
                Charset.Alignment.LEFT);
        charsets[Type.CRITICAL.ordinal()][0] = new Charset(BasicEff.subNode("NoCri1"),
                Charset.Alignment.LEFT);
        charsets[Type.CRITICAL.ordinal()][1] = new Charset( BasicEff.subNode("NoCri0"),
                Charset.Alignment.LEFT);
        charsets[Type.TOPLAYER.ordinal()][0] = new Charset(BasicEff.subNode("NoViolet1"),
                Charset.Alignment.LEFT);
        charsets[Type.TOPLAYER.ordinal()][1] = new Charset( BasicEff.subNode("NoViolet0"),
                Charset.Alignment.LEFT);
    }
}
