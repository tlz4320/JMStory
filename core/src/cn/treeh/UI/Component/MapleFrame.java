package cn.treeh.UI.Component;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapleFrame {
    Texture center;
    Texture east;
    Texture northeast;
    Texture north;
    Texture northwest;
    Texture west;
    Texture southwest;
    Texture south;
    Texture southeast;
    int xtile;
    int ytile;
    public MapleFrame(Node src)
    {
        center = new Texture(src.subNode("c"));
        east = new Texture(src.subNode("e"));
        northeast = new Texture(src.subNode("ne"));
        north = new Texture(src.subNode("n"));
        northwest = new Texture(src.subNode("nw"));
        west = new Texture(src.subNode("w"));
        southwest = new Texture(src.subNode("sw"));
        south = new Texture(src.subNode("s"));
        southeast = new Texture(src.subNode("se"));

        xtile = Math.max(north.dimensions[0], 1);
        ytile = Math.max(north.dimensions[1], 1);
    }
    DrawArg arg;
    public void draw(int[] position, int rwidth, int rheight, SpriteBatch batch)
    {
        int numhor = rwidth / xtile + 2;
        int numver = rheight / ytile;
        int width = numhor * xtile;
        int height = numver * ytile;
        int left = position[0] - width / 2;
        int top = position[1] - height;
        int right = left + width;
        int bottom = top + height;
        arg = new DrawArg(new int[]{left, top});
        northwest.draw(arg, batch);
        arg = new DrawArg(new int[]{left, bottom});
        southwest.draw(arg, batch);

        for (int y = top; y < bottom; y += ytile)
        {
            west.draw(new DrawArg(new int[]{left, y}), batch);
            east.draw(new DrawArg(new int[]{right, y}), batch);
        }

        center.draw( new DrawArg(new int[]{left, top}, new int[]{width, height}), batch);

        for (int x = left; x < right; x += xtile)
        {
            north.draw(new DrawArg(new int[]{x, top}), batch);
            south.draw(new DrawArg(new int[]{x, bottom}), batch);
        }

        northeast.draw(new DrawArg(new int[]{right, top}), batch);
        southeast.draw(new DrawArg(new int[]{right, bottom}), batch);
    }
}
