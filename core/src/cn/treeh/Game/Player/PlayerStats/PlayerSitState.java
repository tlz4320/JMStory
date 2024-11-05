package cn.treeh.Game.Player.PlayerStats;

import cn.treeh.Game.Player.Char;
import cn.treeh.Game.Player.Player;
import cn.treeh.Game.Player.PlayerStat;

public class PlayerSitState extends PlayerStat {
    public void sendAction(Player player, boolean down)
    {
        if (down)
        {
            if(leftInput()){
                player.set_direction(false);
                player.set_state(Char.State.WALK);
            } else if(rightInput()){
                player.set_direction(true);
                player.set_state(Char.State.WALK);
            } else if(jumpInput()){
                player.set_state(Char.State.STAND);
            }

        }
    }
}