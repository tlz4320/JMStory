package cn.treeh.Game.Player.PlayerStats;

import cn.treeh.Game.MapleMap.Ladder;
import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Player;
import cn.treeh.Game.Player.PlayerStat;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.Optional;

public class PlayerNullState extends PlayerStat {
	@Override
	public void sendAction(Player player, boolean down) {
		
	}

	@Override
    public void update_state(Player player) {
        	Char.State state;

		if (player.get_phobj().onground)
		{
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			{
				state = Char.State.WALK;

				player.set_direction(false);
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			{
				state = Char.State.WALK;

				player.set_direction(true);
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
			{
				state = Char.State.PRONE;
			}
			else
			{
				state = Char.State.STAND;
			}
		}
		else
		{
			Ladder ladder = player.getLadder();

			if (ladder != null)
				state = ladder.is_ladder() ? Char.State.LADDER : Char.State.ROPE;
			else
				state = Char.State.FALL;
		}

		player.get_phobj().clear_flags();
		player.set_state(state);
    }
}