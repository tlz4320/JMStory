package cn.treeh.Game.Player.Look;

import cn.treeh.NX.Node;

public class BodyAction {
    Stance.Id stance;
    int frame;
    int delay;
    int[] move;
    boolean attackframe;
    public BodyAction(Node src){
        stance = Stance.byString(src.subNode("action").getString());
        frame = src.subNode("frame").getInt();
        move = src.subNode("move").getVector();
        int sgndelay = src.subNode("delay").getInt();

        if (sgndelay == 0)
            sgndelay = 100;

        if (sgndelay > 0)
        {
            delay = sgndelay;
            attackframe = true;
        }
        else if (sgndelay < 0)
        {
            delay = -sgndelay;
            attackframe = false;
        }
    }
    public boolean isAttack()
    {
        return attackframe;
    }
    int getFrame()
    {
        return frame;
    }

    int getDelay()
    {
        return delay;
    }

    int[] getMove()
    {
        return move;
    }

    Stance.Id getStance()
    {
        return stance;
    }
}
