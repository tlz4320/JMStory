package cn.treeh.Game.Player.PlayerStats;

import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Player;
import cn.treeh.Game.Player.PlayerStat;

public class PlayerFallState  extends PlayerStat {
	@Override
	public void sendAction(Player player, boolean down) {
		
	}

	public void initialize(Player player)
	{
		player.get_phobj().type = PhysicsObject.Type.NORMAL;
	}

	public void update(Player player)
	{
		if (player.is_attacking())
			return;

		double hspeed = player.get_phobj().hspeed;

		if (leftInput() && hspeed > 0.0)
			hspeed -= 0.025;
		else if (rightInput() && hspeed < 0.0)
			hspeed += 0.025;

		if (leftInput())
			player.set_direction(false);
		else if (rightInput())
			player.set_direction(true);
	}

	public void update_state(Player player)
	{
		if (player.get_phobj().onground)
		{
			if (downInput() && !walkInput())
				player.set_state(Char.State.PRONE);
			else
				player.set_state(Char.State.STAND);
		}
		else if (player.is_underwater())
		{
			player.set_state(Char.State.SWIM);
		}
	}
}