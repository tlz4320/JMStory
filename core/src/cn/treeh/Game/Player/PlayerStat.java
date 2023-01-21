package cn.treeh.Game.Player;

import cn.treeh.Audio.SoundPlayer;
import cn.treeh.Game.MapleMap.Ladder;
import cn.treeh.Game.Physics.PhysicsObject;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class PlayerStat extends Actor {
    public void jumpSound() {
        SoundPlayer.play("jump", "Game.img/Jump");
    }

    public boolean walkInput() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) ^ Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    public boolean leftInput() {
        return Gdx.input.isKeyPressed(Input.Keys.LEFT) && !Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }

    public boolean rightInput() {
        return !Gdx.input.isKeyPressed(Input.Keys.LEFT) && Gdx.input.isKeyPressed(Input.Keys.RIGHT);
    }
    public boolean proneInput(){
        return Gdx.input.isKeyPressed(Input.Keys.DOWN) &&
                !Gdx.input.isKeyPressed(Input.Keys.LEFT) &&
                !Gdx.input.isKeyPressed(Input.Keys.RIGHT) &&
                !Gdx.input.isKeyPressed(Input.Keys.UP);

    }
    public boolean downInput(){
        return Gdx.input.isKeyPressed(Input.Keys.DOWN);

    }

    public void updateDef(Player player) {
        Char.State state = null;
        if (player.getPhobj().onground) {
            if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                state = Char.State.WALK;
                player.setDirection(false);
            } else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                state = Char.State.WALK;
                player.setDirection(false);
            } else if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                state = Char.State.PRONE;
            } else {
                state = Char.State.STAND;
            }
        } else {
            Ladder ladder = player.getLadder();
            if(ladder != null)
                state = ladder.is_ladder() ? Char.State.LADDER : Char.State.ROPE;
			else
                state = Char.State.FALL;
        }
        player.getPhobj().clear_flags();
        player.setState(state);
    }
    public void initialize(Player player){
        switch (player.state){
            case STAND:
            case WALK:
                player.getPhobj().type = PhysicsObject.Type.NORMAL;
                break;
        }

    }
    public void update(Player player) {
        Char.State state = player.state;
        switch (state){
            case STAND:
                if(!player.getPhobj().enablejd)
                    player.getPhobj().set_flag(PhysicsObject.Flag.CHECKBELOW);
                if(player.attacking)
                    return;
                if(rightInput()){
                    player.setDirection(true);
                    player.setState(Char.State.WALK);
                } else if(leftInput()){
                    player.setDirection(false);
                    player.setState(Char.State.WALK);
                }
                if(proneInput())
                    player.setState(Char.State.PRONE);
                break;
            case WALK:
                if (!player.getPhobj().enablejd)
                    player.getPhobj().set_flag(PhysicsObject.Flag.CHECKBELOW);
                if (player.attacking)
                    return;
                if(walkInput()){
                    if(rightInput()){
                        player.setDirection(true);
                        player.getPhobj().hforce += player.getWalkSpeed();
                    }
                    else if(leftInput()){
                        player.setDirection(false);
                        player.getPhobj().hforce -= player.getWalkSpeed();
                    }
                }else{
                    if(downInput())
                        player.setState(Char.State.PRONE);
                }
        }
    }
    public void update_state(Player player){
        switch (player.state) {
            case WALK:
                if(player.getPhobj().onground)
                    if (!walkInput() || player.getPhobj().hspeed == 0.0f)
                        player.setState(Char.State.STAND);
            case STAND:
                if (!player.getPhobj().onground)
                    player.setState(Char.State.FALL);
        }
    }
}
