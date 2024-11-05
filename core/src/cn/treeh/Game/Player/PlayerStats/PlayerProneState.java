package cn.treeh.Game.Player.PlayerStats;

import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Player;
import cn.treeh.Game.Player.PlayerStat;

public class PlayerProneState extends PlayerStat {
    public void sendAction(Player player, boolean down)
	{
		if (down && jumpInput())
		{
			if (player.get_phobj().enablejd && downInput())
			{
				jumpSound();

                player.get_phobj().y.set(player.get_phobj().groundbelow);
				player.set_state(Char.State.FALL);
			}
		}
	}

	public void update(Player player)
	{
		if (!player.get_phobj().enablejd)
			player.get_phobj().set_flag(PhysicsObject.Flag.CHECKBELOW);

		if (upInput() || !downInput())
			player.set_state(Char.State.STAND);

		if (leftInput())
		{
			player.set_direction(false);
			player.set_state(Char.State.WALK);
		}

		if (rightInput())
		{
			player.set_direction(true);
			player.set_state(Char.State.WALK);
		}
	}
}