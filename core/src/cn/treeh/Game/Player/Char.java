package cn.treeh.Game.Player;

import cn.treeh.Game.Combat.DamageNumber;
import cn.treeh.Game.MapleMap.MapObject;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Player.Look.AfterImage;
import cn.treeh.Game.Player.Look.CharLook;
import cn.treeh.Game.Player.Look.PetLook;
import cn.treeh.Game.Player.Look.Stance;
import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Effects;
import cn.treeh.Graphics.Text;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.UI.Component.ChatBalloon;
import cn.treeh.Util.Configure;
import cn.treeh.Util.TimedBool;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TextraLabel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class Char extends MapObject {
    enum State {
        //分别数值是245610
        WALK,
        STAND,
        FALL,
        ALERT,
        PRONE,
        SWIM,
        LADDER,
        ROPE,
        DIED,
        SIT
    }

    Effects effects;
    AfterImage afterImage = new AfterImage();
    CharLook look;
    CharLook preLook;
    PetLook[] pets = new PetLook[3];
    State state;
    boolean attacking, facing_right;
    TimedBool invincible = new TimedBool();
    TimedBool ironbody = new TimedBool();
    ChatBalloon chatBalloon = new ChatBalloon();
    TextraLabel namelabel;
    LinkedList<DamageNumber> damages = new LinkedList<>();
    static HashMap<CharEffect.Id, Animation> chareffects = new HashMap<>();
    public Char(int o, CharLook lk, String name) {
        super(o, new int[2]);
        effects = new Effects();
        look = lk;
        preLook = lk;
        namelabel = new TextraLabel(name, Text.texraFont, Color.BLACK);
        namelabel.setAlignment(Align.center);
    }
    DrawArg arg;

    public void draw(double viewx, double viewy, float alpha, SpriteBatch batch){
        int[] absp = phobj.get_absolute(viewx, viewy, alpha);
        effects.drawBelow(absp, alpha, batch);
        Color color = Color.WHITE;
        if(invincible.get()){
            float phi = invincible.alpha() * 30;
            float rgb = (float) (0.9f - 0.5f * Math.abs(Math.sin(phi)));
            color = new Color(rgb, rgb, rgb, 1.0f);
        }
        arg = new DrawArg(absp, color);
        look.draw(arg, alpha, batch);
        arg = new DrawArg(absp, facing_right);
        afterImage.draw(look.getFrame(), arg, alpha, batch);
        if(ironbody.get()){
            float ibalpha = ironbody.alpha();
            float scale = 1.0f + ibalpha;
            float opacity = 1.0f - ibalpha;
            arg = new DrawArg(absp, scale, scale, opacity);
            look.draw(arg, alpha, batch);
        }
        for (PetLook pet : pets)
            if(pet != null)
                pet.draw(viewx, viewy, alpha, batch);
        namelabel.setPosition(absp[0] + 4, absp[1] - 16);
        namelabel.draw(batch, 1.0f);
        effects.drawAbove(absp, alpha, batch);
        absp[1] -= 85;
        chatBalloon.draw(absp, batch);
        for (DamageNumber number : damages)
            number.draw(viewx, viewy, alpha, batch);
    }
    public boolean update(Physics physics, float speed) {
        damages.removeIf(DamageNumber::update);

        effects.update();
        chatBalloon.update();
        invincible.update();
        ironbody.update();

        for (PetLook pet : pets) {
            if (pet.getId() != 0) {
                switch (state) {
                    case LADDER:
                    case ROPE:
                        pet.setStance(PetLook.Stance.HANG);
                        break;
                    case SWIM:
                        pet.setStance(PetLook.Stance.FLY);
                        break;
                    default:
                        if (pet.getStance() == PetLook.Stance.HANG || pet.getStance() == PetLook.Stance.FLY)
                            pet.setStance(PetLook.Stance.STAND);

                        break;
                }

                pet.update(physics, getPosition());
            }
        }

        int stancespeed = 0;

        if (speed >= 1.0f / Configure.TIME_STEP)
            stancespeed = (int) (Configure.TIME_STEP * speed);

        afterImage.update(look.getFrame(), stancespeed);

        return look.update(stancespeed);
    }
    public PhysicsObject getPhobj(){
        return phobj;
    }
    public void setDirection(boolean right){
        if(!attacking){
            facing_right = right;
            look.setDirection(right);
        }
    }

    public boolean isClimbing(){
        return state == State.LADDER || state == State.ROPE;
    }
    @Override
    public int getLayer() {
        return isClimbing() ? 7 : super.getLayer();
    }

    public void setState(State st)
    {
        state = st;

        Stance.Id stance = Stance.byState(state.ordinal());
        look.set_stance(stance);
    }
    public static void init() {
        CharLook.init();

        Node src = NXFiles.Effect().subNode("BasicEff.img");

        for (Map.Entry<CharEffect.Id, String> iter : CharEffect.PATHS.entrySet())
            chareffects.put(iter.getKey(), new Animation(src.subNode(iter.getValue())));
    }
}
