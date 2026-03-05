package cn.treeh.Game.MapleMap;

import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Effects;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.Configure;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class MapEffect {
	boolean active;
	Animation effect;
	int[] position;
	public MapEffect(String path)
	{
		active = false;
		Node Effect = NXFiles.Map().subNode("Effect.img");

		effect = new Animation(Effect.resolve(path));

		int width = Configure.screenWidth;

		position = new int[]{width / 2, 250};
	}

	public MapEffect() {}
	DrawArg arg = new DrawArg();
	public void draw(SpriteBatch batch)
	{
		if (!active)
		{
			arg.pos = position;
			effect.draw(arg, 1.0f, batch);
		}
	}

	public void  update()
	{
		if (!active)
			active = effect.update(6);
	}
}
