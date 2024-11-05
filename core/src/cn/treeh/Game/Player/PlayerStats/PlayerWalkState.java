package cn.treeh.Game.Player.PlayerStats;

import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Player;
import cn.treeh.Game.Player.PlayerStat;

public class PlayerWalkState extends PlayerStat {
    public void initialize(Player player)
	{
		player.get_phobj().type = PhysicsObject.Type.NORMAL;
	}

	public void sendAction(Player player, boolean down)
	{
		if (player.is_attacking())
			return;

		if (down && jumpInput())
		{
			jumpSound();

			player.get_phobj().vforce = -player.get_jumpforce();
		}

		if (down && jumpInput() && downInput() && player.get_phobj().enablejd)
		{
			jumpSound();

			player.get_phobj().y.set(player.get_phobj().groundbelow);
			player.set_state(Char.State.FALL);
		}
	}

	public void update(Player player)
	{
		if (!player.get_phobj().enablejd)
			player.get_phobj().set_flag(PhysicsObject.Flag.CHECKBELOW);

		if (player.is_attacking())
			return;

		if (walkInput())
		{
			if (rightInput())
			{
				player.set_direction(true);
				player.get_phobj().hforce += player.get_walkforce();
			}
			else if (leftInput())
			{
				player.set_direction(false);
				player.get_phobj().hforce += -player.get_walkforce();
			}
		}
		else
		{
			if (downInput())
				player.set_state(Char.State.PRONE);
		}
	}

	public void update_state(Player player)
	{
		if (player.get_phobj().onground)
		{
			if (!walkInput() || player.get_phobj().hspeed == 0.0f)
				player.set_state(Char.State.STAND);
		}
		else
		{
			player.set_state(Char.State.FALL);
		}
	}
}