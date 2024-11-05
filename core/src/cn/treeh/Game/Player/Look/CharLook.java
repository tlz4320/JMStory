package cn.treeh.Game.Player.Look;

import cn.treeh.Audio.SoundPlayer;
import cn.treeh.Game.Data.WeaponData;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.IO.Net.LookEntry;
import cn.treeh.Util.Nominal;
import cn.treeh.Util.TimedBool;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.sql.Time;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Random;
import java.util.TreeMap;

public class CharLook {
    public enum Attack
		{
            NONE,
            S1A1M1D,
            SPEAR,
            BOW,
            CROSSBOW,
			S2A2M2,
			WAND,
			CLAW,
            Useless,
			GUN,
			NUM_ATTACKS
		};
    static Stance.Id[][]degen_stances =
			{
                    { Stance.Id.none },
				{ Stance.Id.none },
				{ Stance.Id.none },
				{ Stance.Id.swingT1, Stance.Id.swingT3 },
				{ Stance.Id.swingT1, Stance.Id.stabT1 },
				{ Stance.Id.none },
				{ Stance.Id.none },
				{ Stance.Id.swingT1, Stance.Id.stabT1 },
				{ Stance.Id.none },
				{ Stance.Id.swingP1, Stance.Id.stabT2 }

            };
    static Stance.Id[][] attack_stances =
			{
				{ Stance.Id.none },
				{ Stance.Id.stabO1, Stance.Id.stabO2,
                Stance.Id.swingO1, Stance.Id.swingO2, Stance.Id.swingO3 },
				{ Stance.Id.stabT1, Stance.Id.swingP1 },
				{ Stance.Id.shoot1 },
				{ Stance.Id.shoot2 },
				{ Stance.Id.stabO1, Stance.Id.stabO2,
                Stance.Id.swingT1, Stance.Id.swingT2, Stance.Id.swingT3 },
				{ Stance.Id.swingO1, Stance.Id.swingO2 },
				{ Stance.Id.swingO1, Stance.Id.swingO2 },
				{ Stance.Id.none },
				{ Stance.Id.shot }
			};
    Nominal<Stance.Id> stance = new Nominal<>();
    Nominal<Integer> stframe = new Nominal<>();
    int stelapsed;

    Nominal<Face.Id> expression = new Nominal<>();
    Nominal<Integer> expframe = new Nominal<>();
    int expelapsed;
    TimedBool expcooldown = new TimedBool();

    boolean flip;

    BodyAction action;
    String actionstr;
    int actframe;

    Body body;
    Hair hair;
    Face face;
    CharEquips equips = new CharEquips();

    Random randomizer = new Random();
    TimedBool alerted = new TimedBool();

    static BodyDrawInfo drawinfo = new BodyDrawInfo();
    static TreeMap<Integer, Hair> hairstyles = new TreeMap<>();
    static TreeMap<Integer, Face> facetypes = new TreeMap<>();
    static TreeMap<Integer, Body> bodytypes = new TreeMap<>();
    public void set_alerted(int millis)
	{
		alerted.setTime(millis);
	}
    public Stance.Id getattackstance(int attack, boolean degenerate)
	{
		if (stance.get() == Stance.Id.prone)
			return Stance.Id.proneStab;

		if (attack <= 0 || attack >= 10)
			return Stance.Id.stand1;

		Stance.Id[] stances = degenerate ? degen_stances[attack] : attack_stances[attack];

		if (stances.length == 0)
			return Stance.Id.stand1;

		int index = randomizer.nextInt(stances.length);

		return stances[index];
	}
    public void set_action(String acstr)
	{
        if (acstr.equals(actionstr) || acstr.isEmpty())
			return;

		Stance.Id ac_stance = Stance.byString(acstr);
        if(ac_stance != Stance.Id.none)
		{
			set_stance(ac_stance);
		}
		else
		{
			action = drawinfo.getAction(acstr, 0);

			if (action != null)
			{
				actframe = 0;
				stelapsed = 0;
				actionstr = acstr;

				stance.set(action.getStance());
				stframe.set(action.getFrame());
			}
		}
	}
    public void set_stance(Stance.Id newstance) {
        if (action != null || newstance == Stance.Id.none)
            return;

        Stance.Id adjstance = equips.adjustStance(newstance);

        if (stance.get() != adjstance) {
            stance.set(adjstance);
            stframe.set(0);
            stelapsed = 0;
        }
    }

    public void set_expression(Face.Id newexpression) {
        if (expression.get() != newexpression && !expcooldown.get()) {
            expression.set(newexpression);
            expframe.set(0);

            expelapsed = 0;
            expcooldown.setTime(5000);
        }
    }

    void reset() {
        flip = true;

        action = null;
        actionstr = "";
        actframe = 0;

        set_stance(Stance.Id.stand1);
        stframe.set(0);
        stelapsed = 0;

        set_expression(Face.Id.def);
        expframe.set(0);
        expelapsed = 0;
    }
    public void set_body(int skin_id) {
        Body iter = bodytypes.get(skin_id);

        if (iter == null) {
            iter = new Body(skin_id, drawinfo);
            bodytypes.put(skin_id, iter);
        }
        body = iter;
    }
    public void setDirection(boolean f){
        flip = f;
    }
    public void set_hair(int hair_id) {
        Hair iter = hairstyles.get(hair_id);

        if (iter == null) {
            iter = new Hair(hair_id, drawinfo);
            hairstyles.put(hair_id, iter);

        }

        hair = iter;
    }
    public void updatetwohanded()
    {
        Stance.Id basestance = Stance.baseof(stance.get());
        set_stance(basestance);
    }
    public void add_equip(int itemid)
    {
        equips.addEquip(itemid, drawinfo);
        updatetwohanded();
    }
    public void set_face(int face_id) {
        Face iter = facetypes.get(face_id);

        if (iter == null) {
            iter = new Face(face_id);
            facetypes.put(face_id, iter);
        }
        face = iter;
    }

    public CharLook(LookEntry entry) {
        reset();
        set_body(entry.skin);
        set_hair(entry.hair);
        set_face(entry.face);
        for(Integer eq : entry.equips.values())
            add_equip(eq);
    }
    public CharLook(){
        reset();
        body = null;
        hair = null;
        face = null;
    }
    void draw(SpriteBatch batch, DrawArg args, Stance.Id interstance, Face.Id interexpression, int interframe, int interexpframe)
    {
        int[] faceshift = drawinfo.getFacePos(interstance, interframe);
        DrawArg faceargs = new DrawArg(args);
        faceargs.pos[0] += (int)(faceshift[0] * faceargs.xscale);
        faceargs.pos[1] += (int)(faceshift[1] * faceargs.yscale);
        if (Stance.isClimbing(interstance))
        {
            body.draw(Body.Layer.BODY, interstance, interframe, args, batch);
            equips.draw(EquipSlot.Id.GLOVES, interstance, Clothing.Layer.GLOVE, interframe, args, batch);
            equips.draw(EquipSlot.Id.SHOES, interstance, Clothing.Layer.SHOES, interframe, args, batch);
            equips.draw(EquipSlot.Id.BOTTOM, interstance, Clothing.Layer.PANTS, interframe, args, batch);
            equips.draw(EquipSlot.Id.TOP, interstance, Clothing.Layer.TOP, interframe, args, batch);
            equips.draw(EquipSlot.Id.TOP, interstance, Clothing.Layer.MAIL, interframe, args, batch);
            equips.draw(EquipSlot.Id.CAPE, interstance, Clothing.Layer.CAPE, interframe, args, batch);
            body.draw(Body.Layer.HEAD, interstance, interframe, args, batch);
            equips.draw(EquipSlot.Id.EARACC, interstance, Clothing.Layer.EARRINGS, interframe, args, batch);

            switch (equips.getcaptype()) {
                case NONE:
                    hair.draw(Hair.Layer.BACK, interstance, interframe, args, batch);
                    break;
                case HEADBAND:
                    equips.draw(EquipSlot.Id.HAT, interstance, Clothing.Layer.CAP, interframe, args, batch);
                    hair.draw(Hair.Layer.BACK, interstance, interframe, args, batch);
                    break;
                case HALFCOVER:
                    hair.draw(Hair.Layer.BELOWCAP, interstance, interframe, args, batch);
                    equips.draw(EquipSlot.Id.HAT, interstance, Clothing.Layer.CAP, interframe, args, batch);
                    break;
                case FULLCOVER:
                    equips.draw(EquipSlot.Id.HAT, interstance, Clothing.Layer.CAP, interframe, args, batch);
                    break;
            }

            equips.draw(EquipSlot.Id.SHIELD, interstance, Clothing.Layer.BACKSHIELD, interframe, args, batch);
            equips.draw(EquipSlot.Id.WEAPON, interstance, Clothing.Layer.BACKWEAPON, interframe, args, batch);
        }
		else
        {
            hair.draw(Hair.Layer.BELOWBODY, interstance, interframe, args, batch);
            equips.draw(EquipSlot.Id.CAPE, interstance, Clothing.Layer.CAPE, interframe, args, batch);
            equips.draw(EquipSlot.Id.SHIELD, interstance, Clothing.Layer.SHIELD_BELOW_BODY, interframe, args, batch);
            equips.draw(EquipSlot.Id.WEAPON, interstance, Clothing.Layer.WEAPON_BELOW_BODY, interframe, args, batch);
            equips.draw(EquipSlot.Id.HAT, interstance, Clothing.Layer.CAP_BELOW_BODY, interframe, args, batch);
            body.draw(Body.Layer.BODY, interstance, interframe, args, batch);
            equips.draw(EquipSlot.Id.GLOVES, interstance, Clothing.Layer.WRIST_OVER_BODY, interframe, args, batch);
            equips.draw(EquipSlot.Id.GLOVES, interstance, Clothing.Layer.GLOVE_OVER_BODY, interframe, args, batch);
            equips.draw(EquipSlot.Id.SHOES, interstance, Clothing.Layer.SHOES, interframe, args, batch);
            body.draw(Body.Layer.ARM_BELOW_HEAD, interstance, interframe, args, batch);

            if (equips.hasOverall())
            {
                equips.draw(EquipSlot.Id.TOP, interstance, Clothing.Layer.MAIL, interframe, args, batch);
            }
            else
            {
                equips.draw(EquipSlot.Id.BOTTOM, interstance, Clothing.Layer.PANTS, interframe, args, batch);
                equips.draw(EquipSlot.Id.TOP, interstance, Clothing.Layer.TOP, interframe, args, batch);
            }

            body.draw(Body.Layer.ARM_BELOW_HEAD_OVER_MAIL, interstance, interframe, args, batch);
            equips.draw(EquipSlot.Id.SHIELD, interstance, Clothing.Layer.SHIELD_OVER_HAIR, interframe, args, batch);
            equips.draw(EquipSlot.Id.EARACC, interstance, Clothing.Layer.EARRINGS, interframe, args, batch);
            body.draw(Body.Layer.HEAD, interstance, interframe, args, batch);
            body.draw(Body.Layer.HUMAN_EAR, interstance, interframe, args.addNew(new int[]{0, 1}), batch);
            hair.draw(Hair.Layer.SHADE, interstance, interframe, args, batch);
            hair.draw(Hair.Layer.DEFAULT, interstance, interframe, args, batch);
            face.draw(interexpression, interexpframe, faceargs, batch);
            equips.draw(EquipSlot.Id.FACE, interstance, Clothing.Layer.FACEACC, 0, faceargs, batch);
            equips.draw(EquipSlot.Id.EYEACC, interstance, Clothing.Layer.EYEACC, interframe, args, batch);
            equips.draw(EquipSlot.Id.SHIELD, interstance, Clothing.Layer.SHIELD, interframe, args, batch);

            switch (equips.getcaptype())
            {
                case NONE:
                hair.draw(Hair.Layer.OVERHEAD, interstance, interframe, args, batch);
                    break;
                case HEADBAND:
                equips.draw(EquipSlot.Id.HAT, interstance, Clothing.Layer.CAP, interframe, args, batch);
                    hair.draw(Hair.Layer.DEFAULT, interstance, interframe, args, batch);
                    hair.draw(Hair.Layer.OVERHEAD, interstance, interframe, args, batch);
                    equips.draw(EquipSlot.Id.HAT, interstance, Clothing.Layer.CAP_OVER_HAIR, interframe, args, batch);
                    break;
                case HALFCOVER:
                hair.draw(Hair.Layer.DEFAULT, interstance, interframe, args, batch);
                    equips.draw(EquipSlot.Id.HAT, interstance, Clothing.Layer.CAP, interframe, args, batch);
                    break;
                case FULLCOVER:
                equips.draw(EquipSlot.Id.HAT, interstance, Clothing.Layer.CAP, interframe, args, batch);
                    break;
            }

            equips.draw(EquipSlot.Id.WEAPON, interstance, Clothing.Layer.WEAPON_BELOW_ARM, interframe, args, batch);

            if (twoHanded(interstance))
            {
                body.draw(Body.Layer.ARM, interstance, interframe, args, batch);
                equips.draw(EquipSlot.Id.TOP, interstance, Clothing.Layer.MAILARM, interframe, args, batch);
                equips.draw(EquipSlot.Id.WEAPON, interstance, Clothing.Layer.WEAPON, interframe, args, batch);
            }
            else
            {
                equips.draw(EquipSlot.Id.WEAPON, interstance, Clothing.Layer.WEAPON, interframe, args, batch);
                body.draw(Body.Layer.ARM, interstance, interframe, args, batch);
                equips.draw(EquipSlot.Id.TOP, interstance, Clothing.Layer.MAILARM, interframe, args, batch);
            }
//
            equips.draw(EquipSlot.Id.GLOVES, interstance, Clothing.Layer.WRIST, interframe, args, batch);
            equips.draw(EquipSlot.Id.GLOVES, interstance, Clothing.Layer.GLOVE, interframe, args, batch);
            equips.draw(EquipSlot.Id.WEAPON, interstance, Clothing.Layer.WEAPON_OVER_GLOVE, interframe, args, batch);

            body.draw(Body.Layer.HAND_BELOW_WEAPON, interstance, interframe, args, batch);

            body.draw(Body.Layer.ARM_OVER_HAIR, interstance, interframe, args, batch);
            body.draw(Body.Layer.ARM_OVER_HAIR_BELOW_WEAPON, interstance, interframe, args, batch);
            equips.draw(EquipSlot.Id.WEAPON, interstance, Clothing.Layer.WEAPON_OVER_HAND, interframe, args, batch);
            equips.draw(EquipSlot.Id.WEAPON, interstance, Clothing.Layer.WEAPON_OVER_BODY, interframe, args, batch);
            body.draw(Body.Layer.HAND_OVER_HAIR, interstance, interframe, args, batch);
            body.draw(Body.Layer.HAND_OVER_WEAPON, interstance, interframe, args, batch);

            equips.draw(EquipSlot.Id.GLOVES, interstance, Clothing.Layer.WRIST_OVER_HAIR, interframe, args, batch);
            equips.draw(EquipSlot.Id.GLOVES, interstance, Clothing.Layer.GLOVE_OVER_HAIR, interframe, args, batch);
        }
    }
    public void draw(int[] position, boolean flipped, Stance.Id interstance, Face.Id interexpression, SpriteBatch batch) {
        interstance = equips.adjustStance(interstance);
        draw(batch, new DrawArg(position, flipped), interstance, interexpression, 0, 0);
    }
    public void draw(DrawArg args, float alpha, SpriteBatch batch)
    {
        if (body == null || hair == null || face == null)
            return;

        int[] acmove = new int[2];

        if (action != null)
            acmove = action.getMove();

        DrawArg relargs = args.addNew(acmove);

        Stance.Id interstance = stance.get(alpha);
        Face.Id interexpression = expression.get(alpha);
        int interframe = stframe.get(alpha);
        int interexpframe = expframe.get(alpha);

        switch (interstance)
        {
            case stand1:
            case stand2:
            {
                if (alerted != null)
                    interstance = Stance.Id.alert;

                break;
            }
        }
        relargs.setFlip(flip);

        draw(batch, relargs, interstance, interexpression, interframe, interexpframe);
    }
    public boolean twoHanded(Stance.Id st)
    {
        switch (st)
        {
            case stand1:
            case walk1:
            return false;
            case stand2:
            case walk2:
            return true;
            default:
                return equips.isTwohanded();
        }
    }
    int get_delay(Stance.Id st, int fr)
    {
        return drawinfo.getDelay(st, fr);
    }
    int getnextframe(Stance.Id st, int fr)
    {
        return drawinfo.nextframe(st, fr);
    }
    public int getFrame(){
        return stframe.get();
    }
    public boolean update(int timestep) {
        if (timestep == 0) {
            stance.normalize();
            stframe.normalize();
            expression.normalize();
            expframe.normalize();
            return false;
        }

        alerted.update();
        expcooldown.update();

        boolean aniend = false;

        if (action == null) {
            int delay = get_delay(stance.get(), stframe.get());
            int delta = delay - stelapsed;

            if (timestep >= delta) {
                stelapsed = timestep - delta;

                int nextframe = getnextframe(stance.get(), stframe.get());
                float threshold = (float) delta / timestep;
                stframe.next(nextframe, threshold);

                if (stframe.get() == 0)
                    aniend = true;
            } else {
                stance.normalize();
                stframe.normalize();

                stelapsed += timestep;
            }
        } else {
            int delay = action.getDelay();
            int delta = delay - stelapsed;

            if (timestep >= delta) {
                stelapsed = timestep - delta;
                actframe = drawinfo.next_actionframe(actionstr, actframe);

                if (actframe > 0) {
                    action = drawinfo.getAction(actionstr, actframe);

                    float threshold = (float) delta / timestep;
                    stance.next(action.getStance(), threshold);
                    stframe.next(action.getFrame(), threshold);
                } else {
                    aniend = true;
                    action = null;
                    actionstr = "";
                    set_stance(Stance.Id.stand1);
                }
            } else {
                stance.normalize();
                stframe.normalize();

                stelapsed += timestep;
            }
        }

        int expdelay = face.getDelay(expression.get(), expframe.get());
        int expdelta = expdelay - expelapsed;

        if (timestep >= expdelta) {
            expelapsed = timestep - expdelta;

            int nextexpframe = face.nextFrame(expression.get(), expframe.get());
            float fcthreshold = (float) expdelta / timestep;
            expframe.next(nextexpframe, fcthreshold);

            if (expframe.get() == 0) {
                if (expression.get() == Face.Id.def)
                    expression.next(Face.Id.blink, fcthreshold);
                else
                    expression.next(Face.Id.def, fcthreshold);
            }
        } else {
            expression.normalize();
            expframe.normalize();

            expelapsed += timestep;
        }

        return aniend;
    }
    public static void init(){
        drawinfo.init();
    }
    public void remove_equip(EquipSlot.Id slot)
	{
		equips.remove_equip(slot);

		if (slot == EquipSlot.Id.WEAPON)
			updatetwohanded();
	}
    public void attack(boolean degenerate)
	{
		int weapon_id = equips.getWeapon();

		if (weapon_id <= 0)
			return;

		WeaponData weapon = WeaponData.get(weapon_id);

		int attacktype = weapon.get_attack();

		if (attacktype == 9 && !degenerate)
		{
			stance.set(Stance.Id.shot);
			set_action("handgun");
		}
		else
		{
			stance.set(getattackstance(attacktype, degenerate));
			stframe.set(0);
			stelapsed = 0;
		}

        SoundPlayer.play(weapon.get_usesound(degenerate));
	}

	public void attack(Stance.Id newstance)
	{
		if (action != null || newstance == Stance.Id.none)
			return;

        if (Objects.requireNonNull(newstance) == Stance.Id.shot) {
            set_action("handgun");
        } else {
            set_stance(newstance);
        }
	}

	public int get_attackdelay(int no, int first_frame)
	{
		if (action != null)
		{
			return drawinfo.getAttackDelay(actionstr, no);
		}
		else
		{
			int delay = 0;

			for (int frame = 0; frame < first_frame; frame++)
				delay += get_delay(stance.get(), frame);
			return delay;
		}
	}

    public CharEquips getEquips() {
        return equips;
    }
    public Stance.Id getStrance(){
        return stance.get();
    }
}
