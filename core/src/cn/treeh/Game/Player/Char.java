package cn.treeh.Game.Player;

import cn.treeh.Game.Combat.DamageNumber;
import cn.treeh.Game.MapleMap.MapObject;
import cn.treeh.Game.Physics.Physics;
import cn.treeh.Game.Physics.PhysicsObject;
import cn.treeh.Game.Player.Look.*;
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
        SIT;
        public static State by_value(int v){
            return State.values()[(v - 1) / 2];
        }
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
    public void setState(int statebyte){
        if (statebyte % 2 == 1)
		{
			setDirection(false);

			statebyte -= 1;
		}
		else
		{
			setDirection(true);
		}

		Char.State newstate = Char.State.by_value(statebyte);
		setState(newstate);
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

    public float getStancespeed()
	{
		if (attacking)
			return getRealAttackspeed();

		switch (state)
		{
		case WALK:
            return (float)Math.abs(phobj.hspeed);
		case LADDER:
		case ROPE:
            return (float)Math.abs(phobj.vspeed);
		default:
			return 1.0f;
		}
	}

	public float getRealAttackspeed()
	{
		int speed = get_integer_attackspeed();

		return 1.7f - speed / 10f;
	}

	public int getAttackdelay(int no)
	{
		int first_frame = afterImage.get_first_frame();
		int delay = look.get_attackdelay(no, first_frame);
		float fspeed = getRealAttackspeed();

        return (int)(delay / fspeed);
	}
    public int update(Physics physics)
    	{
    		update(physics, 1.0f);

    		return getLayer();
    	}
    public void showAttackEffect(Animation toshow, int z)
    	{
    		float attackspeed = getRealAttackspeed();
    		effects.add(toshow, new DrawArg(facing_right), z, attackspeed);
    	}

    	void showEffectId(CharEffect.Id toshow)
    	{
            effects.add(chareffects.get(toshow));
    	}

    	public void showIronBody()
    	{
    		ironbody.setTime(500);
    	}

    	public void showDamage(int damage)
    	{
    		int start_y = phobj.get_y() - 60;
    		int x = phobj.get_x() - 10;

            damages.add(new DamageNumber(DamageNumber.Type.TOPLAYER, damage, start_y, x));

    		look.set_alerted(5000);
    		invincible.setTime(2000);
    	}

    	public void speak(String line)
    	{
    		chatBalloon.changeText(line);
    	}

    	public void changeLook(MapleStat.Id stat, int id)
    	{
    		switch (stat)
    		{
    		case SKIN:
    			look.set_body(id);
    			break;
    		case FACE:
    			look.set_face(id);
    			break;
    		case HAIR:
    			look.set_hair(id);
    			break;
    		}
    	}


    	public void setExpression(int expid)
    	{
    		Face.Id expression = Face.Id.byAction(expid);
    		look.set_expression(expression);
    	}

    	public void attack(String action)
    	{
    		look.set_action(action);

    		attacking = true;
    		look.set_alerted(5000);
    	}

    	public void attack(Stance.Id stance)
    	{
    		look.attack(stance);

    		attacking = true;
    		look.set_alerted(5000);
    	}

    	void Char::attack(bool degenerate)
    	{
    		look.attack(degenerate);

    		attacking = true;
    		look.set_alerted(5000);
    	}

    	void Char::set_afterimage(int32_t skill_id)
    	{
    		int32_t weapon_id = look.get_equips().get_weapon();

    		if (weapon_id <= 0)
    			return;

    		const WeaponData& weapon = WeaponData::get(weapon_id);

    		std::string stance_name = Stance::names[look.get_stance()];
    		int16_t weapon_level = weapon.get_equipdata().get_reqstat(MapleStat::Id::LEVEL);
    		const std::string& ai_name = weapon.get_afterimage();

    		afterimage = Afterimage(skill_id, ai_name, stance_name, weapon_level);
    	}

    	const Afterimage& Char::get_afterimage() const
    	{
    		return afterimage;
    	}

    	void Char::set_direction(bool f)
    	{
    		facing_right = f;
    		look.set_direction(f);
    	}

    	void Char::set_state(State st)
    	{
    		state = st;

    		Stance::Id stance = Stance::by_state(state);
    		look.set_stance(stance);
    	}

    	void Char::add_pet(uint8_t index, int32_t iid, const std::string& name, int32_t uniqueid, Point<int16_t> pos, uint8_t stance, int32_t fhid)
    	{
    		if (index > 2)
    			return;

    		pets[index] = PetLook(iid, name, uniqueid, pos, stance, fhid);
    	}

    	void Char::remove_pet(uint8_t index, bool hunger)
    	{
    		if (index > 2)
    			return;

    		pets[index] = PetLook();

    		if (hunger)
    		{
    			// TODO: Empty
    		}
    	}

    	bool Char::is_invincible() const
    	{
    		return invincible == true;
    	}

    	bool Char::is_sitting() const
    	{
    		return state == State::SIT;
    	}

    	bool Char::is_climbing() const
    	{
    		return state == State::LADDER || state == State::ROPE;
    	}

    	bool Char::is_twohanded() const
    	{
    		return look.get_equips().is_twohanded();
    	}

    	Weapon::Type Char::get_weapontype() const
    	{
    		int32_t weapon_id = look.get_equips().get_weapon();

    		if (weapon_id <= 0)
    			return Weapon::Type::NONE;

    		return WeaponData::get(weapon_id).get_type();
    	}

    	bool Char::getflip() const
    	{
    		return facing_right;
    	}

    	std::string Char::get_name() const
    	{
    		return namelabel.get_text();
    	}

    	CharLook& Char::get_look()
    	{
    		return look;
    	}

    	const CharLook& Char::get_look() const
    	{
    		return look;
    	}

    	PhysicsObject& Char::get_phobj()
    	{
    		return phobj;
    	}

    	void Char::init()
    	{
    		CharLook::init();

    		nl::node src = nl::nx::Effect["BasicEff.img"];

    		for (auto iter : CharEffect::PATHS)
    			chareffects.emplace(iter.first, src.resolve(iter.second));
    	}

    	EnumMap<CharEffect::Id, Animation> Char::chareffects;
}
