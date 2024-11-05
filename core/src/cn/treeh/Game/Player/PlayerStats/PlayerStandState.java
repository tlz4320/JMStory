package cn.treeh.Game.Player.PlayerStats;

import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Player;
import cn.treeh.Game.Player.PlayerStat;

public class PlayerStandState extends PlayerStat {
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
	}

	public void update(Player player)
	{
		if (!player.get_phobj().enablejd)
			player.get_phobj().set_flag(PhysicsObject.Flag.CHECKBELOW);

		if (player.is_attacking())
			return;

		if (rightInput())
		{
			player.set_direction(true);
			player.set_state(Char.State.WALK);
		}
		else if (leftInput())
		{
			player.set_direction(false);
			player.set_state(Char.State.WALK);
		}

		if (proneInput())
			player.set_state(Char.State.PRONE);
	}

	public void update_state(Player player)
	{
		if (!player.get_phobj().onground)
			player.set_state(Char.State.FALL);
	}
}