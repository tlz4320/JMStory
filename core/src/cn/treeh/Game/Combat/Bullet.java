package cn.treeh.Game.Combat;

import cn.treeh.Game.Physics.MovingObject;
import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Bullet {
    Animation animation;
		MovingObject moveobj;
		boolean flip;
    public  Bullet(Animation a, int[] origin, boolean toleft)
	{
		animation = a;

		moveobj.set_x(origin[0] + (toleft ? -30.0 : 30.0));
		moveobj.set_y(origin[1] - 26.0);
	}

	public void draw(double viewx, double viewy, float alpha, SpriteBatch batch)
	{
		int[] bulletpos = moveobj.get_absolute(viewx, viewy, alpha);
		DrawArg args = new DrawArg(bulletpos, flip);
		animation.draw(args, alpha, batch);
	}

	public boolean settarget(int[] target)
	{
		double xdelta = target[0] - moveobj.crnt_x();
		double ydelta = target[1] - moveobj.crnt_y();

		if (Math.abs(xdelta) < 10.0)
			return true;

		flip = xdelta > 0.0;

		moveobj.hspeed = xdelta / 32;

		if (xdelta > 0.0)
		{
			if (moveobj.hspeed < 3.0)
				moveobj.hspeed = 3.0;
			else if (moveobj.hspeed > 6.0)
				moveobj.hspeed = 6.0;
		}
		else if (xdelta < 0.0)
		{
			if (moveobj.hspeed > -3.0)
				moveobj.hspeed = -3.0;
			else if (moveobj.hspeed < -6.0)
				moveobj.hspeed = -6.0;
		}

		moveobj.vspeed = moveobj.hspeed * ydelta / xdelta;

		return false;
	}

	public boolean update(int[] target)
	{
		animation.update();
		moveobj.move();

		int xdelta = target[0] - moveobj.get_x();
		return moveobj.hspeed > 0.0 ? xdelta < 10 : xdelta > 10;
	}
}