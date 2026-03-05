package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Graphics.Rectangle;
import cn.treeh.Util.InterScaleD;

public abstract class Drop extends MapObject{
	public enum State
			{
				DROPPED,
				FLOATING,
				PICKEDUP
			};

	int owner;
	int pickuptype;
	boolean playerdrop;

	PhysicsObject looter;
	State state;

	int[] dest;
	double basey;
	double moved;
	InterScaleD opacity = new InterScaleD();
	InterScaleD angle = new InterScaleD();
	public Drop(int id, int own, int[] start, int[] dst, int type, int mode, boolean pldrp)
	{
		super(id, new int[]{0,0});
		owner = own;
		setPos(start[0], start[1] - 4);
		dest = dst;
		pickuptype = type;
		playerdrop = pldrp;

		angle.set(0.0f);
		opacity.set(1.0f);
		moved = 0.0f;
		looter = null;

		switch (mode)
		{
			case 0:
				case 1:
					state = State.DROPPED;
					basey = dest[1] - 4;
					phobj.vspeed = -5.0f;
					phobj.hspeed = (dest[0] - start[0]) / 48d;
					break;
					case 2:
						state = State.FLOATING;
						basey = phobj.crnt_y();
						phobj.type = PhysicsObject.Type.FIXATED;
						break;
						case 3:
							state = State.PICKEDUP;
							phobj.vspeed = -5.0f;
							break;
		}
	}
	static float SPINSTEP = 0.2f;
	static int PICKUPTIME = 48;
	static float OPCSTEP = 1.0f / PICKUPTIME;
	public int update( Physics physics)
	{
		physics.move(phobj);

		if (state == State.DROPPED)
		{
			if (phobj.onground)
			{
				phobj.hspeed = 0.0;
				phobj.type = PhysicsObject.Type.FIXATED;
				state = State.FLOATING;
				angle.set(0.0f);
				setPos(dest[0], dest[1] - 4);
			}
			else
			{

				angle.add(SPINSTEP);
			}
		}

		if (state == State.FLOATING)
		{
			phobj.y.set(basey + 5.0f + (Math.cos(moved) - 1.0f) * 2.5f);
			moved = (moved < 360.0f) ? moved + 0.025f : 0.0f;
		}

		if (state == State.PICKEDUP)
		{

			if (looter != null)
			{
				double hdelta = looter.x.get() - phobj.x.get();
				phobj.hspeed = looter.hspeed / 2.0 + (hdelta - 16.0) / PICKUPTIME;
			}

			opacity.sub(OPCSTEP);

			if (opacity.last() <= OPCSTEP)
			{
				opacity.set(1.0f);

				super.deActivate();
				return -1;
			}
		}

		return phobj.fhlayer;
	}

	public void expire(int type,  PhysicsObject lt)
	{
		switch (type)
		{
			case 0:
				state = State.PICKEDUP;
				break;
				case 1:
					deActivate();
					break;
					case 2:
						angle.set(0.0f);
						state = State.PICKEDUP;
						looter = lt;
						phobj.vspeed = -4.5f;
						phobj.type = PhysicsObject.Type.NORMAL;
						break;
		}
	}

	public Rectangle bounds()
	{
		int[] lt = getPosition();
		int[] rb = new int[]{lt[0] + 32, lt[1] + 32};

		return new Rectangle(lt, rb);
	}
}
