package cn.treeh.Game.Player.PlayerStats;

import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Player;
import cn.treeh.Game.Player.PlayerStat;

public class PlayerFlyState  extends PlayerStat {
    public void initialize(Player player)
	{
		player.get_phobj().type = player.is_underwater() ? PhysicsObject.Type.SWIMMING : PhysicsObject.Type.FLYING;
	}

	public void sendAction(Player player, boolean down)
	{
		if (down)
		{
            if(leftInput()){
                player.set_direction(false);

            } else if(rightInput()){
                player.set_direction(true);
            }
		}
	}

	public void update(Player player)
	{
		if (player.is_attacking())
			return;

		if (leftInput())
			player.get_phobj().hforce = -player.get_flyforce();
		else if (rightInput())
			player.get_phobj().hforce = player.get_flyforce();

		if (upInput())
			player.get_phobj().vforce = -player.get_flyforce();
		else if (downInput())
			player.get_phobj().vforce = player.get_flyforce();
	}

	public void update_state(Player player)
	{
		if (player.get_phobj().onground && player.is_underwater())
		{
			Char.State state;

			if (leftInput())
			{
				state = Char.State.WALK;

				player.set_direction(false);
			}
			else if (rightInput())
			{
				state = Char.State.WALK;

				player.set_direction(true);
			}
			else if (downInput())
			{
				state = Char.State.PRONE;
			}
			else
			{
				state = Char.State.STAND;
			}

			player.set_state(state);
		}
	}
}