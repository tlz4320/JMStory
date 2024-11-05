package cn.treeh.Game.Player.PlayerStats;

import cn.treeh.Game.MapleMap.Ladder;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Player;
import cn.treeh.Game.Player.PlayerStat;

public class PlayerClimbState extends PlayerStat {
	@Override
	public void sendAction(Player player, boolean down) {
		
	}

	public void initialize(Player player)
	{
		player.get_phobj().type = PhysicsObject.Type.FIXATED;
	}

	public void update(Player player)
	{
		if (upInput() && !downInput())
		{
			player.get_phobj().vspeed = -player.get_climbforce();
		}
		else if (downInput() && !upInput())
		{
			player.get_phobj().vspeed = player.get_climbforce();
		}
		else
		{
			player.get_phobj().vspeed = 0.0;
		}

		if (downInput() && walkInput())
		{
			jumpSound();

			 double walkforce = player.get_walkforce() * 8.0;

			player.set_direction(rightInput());

			player.get_phobj().hspeed = leftInput() ? -walkforce : walkforce;
			player.get_phobj().vspeed = -player.get_jumpforce() / 1.5;

			cancel_ladder(player);
		}
	}

	public void update_state(Player player)
	{
		int y = player.get_phobj().get_y();
		Ladder ladder = player.getLadder();

		if (ladder != null && ladder.felloff(y, downInput()))
			cancel_ladder(player);
	}

	void cancel_ladder(Player player)
	{
		player.set_state(Char.State.FALL);
		player.set_ladder(null);
		player.set_climb_cooldown();
	}
}