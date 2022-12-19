package cn.treeh.Game;

import cn.treeh.Util.Configure;
import cn.treeh.Util.InterScaleD;

public class Camera {
    InterScaleD x;
    InterScaleD y;

    // View limits.
    int[] hbounds;
    int[] vbounds;

    int VWIDTH;
    int VHEIGHT;
    public Camera(){
        x = new InterScaleD();
        y = new InterScaleD();
        x.set(0);
        y.set(0);
        hbounds = new int[2];
        vbounds = new int[2];
        VWIDTH = Configure.screenWidth;
        VHEIGHT = Configure.screenHeight;
    }
    public void setPos(int[] pos){
        int new_width = Configure.screenWidth;
        int new_height = Configure.screenHeight;
        if(VHEIGHT != new_height || VWIDTH != new_width){
            VWIDTH = new_width;
            VHEIGHT = new_height;
        }
        x.set((double) VWIDTH / 2 - pos[0]);
        y.set((double) VHEIGHT / 2 - pos[1]);
    }
    public void setView(int[] mapWalls, int[] mapBorders){
        hbounds = mapWalls;
        vbounds = mapBorders;
        hbounds[0] = -hbounds[0];
        hbounds[1] = -hbounds[1];
        vbounds[0] = -vbounds[0];
        vbounds[1] = -vbounds[1];
    }
    public int[] position(){
        return new int[]{(int) Math.round(x.get()), (int) Math.round(y.get())};
    }
    public int[] position(float alpha){
        return new int[]{(int) Math.round(x.get(alpha)), (int) Math.round(y.get(alpha))};
    }
    public double[] realPosition(float alpha){
        return new double[] { x.get(alpha), y.get(alpha) };
    }
    public void update(int[] position){
        int new_width = Configure.screenWidth;
        int new_height = Configure.screenHeight;

        if (VWIDTH != new_width || VHEIGHT != new_height)
        {
            VWIDTH = new_width;
            VHEIGHT = new_height;
        }

        double next_x = x.get();
        double hdelta = (double) VWIDTH / 2 - position[0] - next_x;

        if (Math.abs(hdelta) >= 5.0)
            next_x += hdelta * (12.0 / VWIDTH);

        double next_y = y.get();
        double vdelta = (double) VHEIGHT / 2 - position[1] - next_y;

        if (Math.abs(vdelta) >= 5.0)
            next_y += vdelta * (12.0 / VHEIGHT);

        if (next_x > hbounds[0] || Math.abs(hbounds[1] - hbounds[0])< VWIDTH)
            next_x = hbounds[0];
        else if (next_x < hbounds[1] + VWIDTH)
            next_x = hbounds[1] + VWIDTH;

        if (next_y > vbounds[0] || Math.abs(vbounds[1] - vbounds[0]) < VHEIGHT)
            next_y = vbounds[0];
        else if (next_y < vbounds[1] + VHEIGHT)
            next_y = vbounds[1] + VHEIGHT;

        x.equal(next_x);
        y.equal(next_y);
    }

}
