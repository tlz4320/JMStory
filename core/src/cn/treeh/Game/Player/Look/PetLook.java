package cn.treeh.Game.Player.Look;

import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Text;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.tommyettinger.textra.TypingLabel;

import java.util.HashMap;

public class PetLook {
    public enum Stance
    {
        MOVE,
                STAND,
                JUMP,
                ALERT,
                PRONE,
                FLY,
                HANG,
                WARP,
                LENGTH
    };
    int itemid;
    String name;
    int uniqueid;
    Stance stance;
    boolean flip;

    HashMap<Stance, Animation> animations = new HashMap<>();
    PhysicsObject phobj;
    TypingLabel label;
    public void setPos(int[] pos){
        phobj.set_x(pos[0]);
        phobj.set_y(pos[1]);
    }
    public void setStance(Stance st)
    {
        if (stance != st )
        {
            stance = st;
            if(animations.containsKey(st))
                animations.get(stance).reset();
        }
    }
    public Stance getStance(){
        return stance;
    }
    public PetLook(int iid, String nm, int uqid, int[] pos,
                   Stance st, int ext)
    {
        itemid = iid;
        name = nm;
        uniqueid = uqid;
        phobj = new PhysicsObject();
        setPos(pos);
        setStance(st);

        Node src = NXFiles.Item().subNode("Pet")
            .subNode(iid + ".img");

        animations.put(Stance.MOVE, new Animation(src.subNode("move")));
        animations.put(Stance.STAND,new Animation(src.subNode("stand0")));
        animations.put(Stance.JUMP,new Animation(src.subNode("jump")));
        animations.put(Stance.ALERT,new Animation(src.subNode("alert")));
        animations.put(Stance.PRONE,new Animation(src.subNode("prone")));
        animations.put(Stance.FLY,new Animation(src.subNode("fly")));
        animations.put(Stance.HANG,new Animation(src.subNode("hang")));

        Node effsrc = NXFiles.Effect().subNode("PetEff.img").subNode("" + iid);

        animations.put(Stance.WARP,new Animation(effsrc.subNode("warp")));
        label = new TypingLabel(name, Text.texraFont);
    }

    public PetLook()
    {
        itemid = 0;
        name = "";
        uniqueid = 0;
        stance = Stance.STAND;
    }
    public int getId(){
        return itemid;
    }
    public void draw(double viewx, double viewy, float alpha, SpriteBatch batch)
    {
        int[] absp = phobj.get_absolute(viewx, viewy, alpha);

        animations.get(stance).draw(new DrawArg(absp, flip), alpha, batch);
        label.setPosition(absp[0], absp[1]);
        label.draw(batch, alpha);
    }
    double distance(int[] x, int[] y){
        return Math.sqrt(Math.pow(x[0] - y[0], 2) + Math.pow(x[1] - y[1], 2));
    }
    public void update(Physics physics, int[] charpos)
    {
        double PETWALKFORCE = 0.35;
        double PETFLYFORCE = 0.2;

        int[] curpos = phobj.get_position();

        switch (stance)
        {
            case STAND:
            case MOVE:
                if (distance(curpos, charpos) > 150)
                {
                    setPos(charpos);
                }
                else
                {
                    if (charpos[0] - curpos[0] > 50)
                    {
                        phobj.hforce = PETWALKFORCE;
                        flip = true;

                        setStance(Stance.MOVE);
                    }
                    else if (charpos[0] - curpos[0] < -50)
                    {
                        phobj.hforce = -PETWALKFORCE;
                        flip = false;

                        setStance(Stance.MOVE);
                    }
                    else
                    {
                        phobj.hforce = 0.0;

                        setStance(Stance.STAND);
                    }
                }

                phobj.type = PhysicsObject.Type.NORMAL;
                phobj.clear_flag(PhysicsObject.Flag.NOGRAVITY);
                break;
            case HANG:
                setPos(charpos);
                phobj.set_flag(PhysicsObject.Flag.NOGRAVITY);
                break;
            case FLY:
                if (distance(charpos, curpos) > 250)
                {
                    setPos(charpos);
                }
                else
                {
                    if (charpos[0] - curpos[0] > 50)
                    {
                        phobj.hforce = PETFLYFORCE;
                        flip = true;
                    }
                    else if (charpos[0] - curpos[0] < -50)
                    {
                        phobj.hforce = -PETFLYFORCE;
                        flip = false;
                    }
                    else
                    {
                        phobj.hforce = 0.0f;
                    }

                    if (charpos[1] - curpos[1] > 50.0f)
                        phobj.vforce = PETFLYFORCE;
                    else if (charpos[1] - curpos[1] < -50.0f)
                        phobj.vforce = -PETFLYFORCE;
                    else
                        phobj.vforce = 0.0f;
                }

                phobj.type = PhysicsObject.Type.FLYING;
                phobj.clear_flag(PhysicsObject.Flag.NOGRAVITY);
                break;
        }

        physics.move(phobj);

        animations.get(stance).update();
    }
}
