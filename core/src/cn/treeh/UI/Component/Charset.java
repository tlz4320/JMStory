package cn.treeh.UI.Component;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Iterator;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class Charset {
    public enum Alignment {
        LEFT,
        CENTER,
        RIGHT
    }

    ;
    TreeMap<Integer, Texture> chars = new TreeMap<>();
    Alignment alignment;

    public Charset(Node src, Alignment alignment) {
        this.alignment = alignment;
        for (int i = 0; i < src.nChild(); i++) {
            Node node = src.subNode(i);
            String name = node.getName();

            if (node.getType() != Node.Type.bitmap || name.equals(""))
                continue;

            char c = name.charAt(0);

            if (c == '\\')
                c = '/';

            chars.put((int)c, new Texture(node));
        }
    }


    public void draw(int c, DrawArg arg, SpriteBatch batch)
    {
        Texture iter = chars.get(c);

        if (iter != null)
            iter.draw(arg, batch);
    }

    public int getw(int c)
    {
        Texture iter = chars.get(c);

        if (iter != null)
            return iter.dimensions[0];

        return 0;
    }

    // TODO: The two below draw methods need combined adding hspace to width only if it does not equal zero
    public int draw( String str,  DrawArg args, SpriteBatch batch)
    {
        int shift = 0;
        int total = 0;

        switch (alignment)
        {
            case CENTER: {
                Iterator<Integer> text = str.chars().iterator();
                while (text.hasNext()) {
                    int c = text.next();
                    int width = getw(c);

                    args.addPos(shift, 0);
                    draw(c, args, batch);

                    shift += width + 2;
                    total += width;
                }
                shift -= total / 2;
                break;
            }
            case LEFT:
            {
                Iterator<Integer> text = str.chars().iterator();
                while (text.hasNext()) {
                    int c = text.next();
                    args.addPos(shift, 0);
                    draw(c, args, batch);

                    shift += getw(c) + 1;
                }

                break;
            }
            case RIGHT:
            {
                char[] text = str.toCharArray();
                for (int i = text.length - 1; i >= 0; i--)
                {

                    int c = text[i];
                    shift += getw(c);
                    args.addPos(-shift, 0);
                    draw(c, args,batch);
                }

                break;
            }
        }

        return shift;
    }

}
