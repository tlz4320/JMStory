package cn.treeh.Game.MapleMap;

import cn.treeh.Game.Physics.Physics;
import cn.treeh.Graphics.Animation;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Rectangle;
import cn.treeh.Graphics.Text;
import cn.treeh.JMStory;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.UI.Component.Button;
import cn.treeh.UI.Component.Component;
import cn.treeh.UI.Component.Cursor;
import cn.treeh.UI.Component.NameLabel;
import cn.treeh.Util.StringUtil;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.github.tommyettinger.textra.TypingLabel;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

public class Npc extends MapObject {
	static class InvisibleButton extends Component{
		Npc npc;

		public InvisibleButton(Npc n) {
			super(JMStory.batch, JMStory.stage);
			npc = n;
			addListener(new ClickListener(){
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
				if(npc != null && npc.isActive()){
					Cursor.get().setState(Cursor.State.CLICKING);
				}

                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void clicked(InputEvent event, float x, float y) {
				if(npc != null && npc.isActive()){
//					TalkToNPCPacket(npc.getOid()).dispatch();
				}
//                Cursor.get().setState(Cursor.State.CANCLICK);
				Cursor.get().setState(Cursor.State.IDLE);
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                super.exit(event, x, y, pointer, toActor);
				Cursor.get().setState(Cursor.State.IDLE);
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor toActor) {
                if(!isPressed()) {
					Cursor.get().setState(Cursor.State.CANCLICK);
                }
            }
        });
		}
	}

	HashMap<String, Animation> animations = new HashMap<>();
	HashMap<String, LinkedList<String>> lines = new HashMap<>();
	LinkedList<String> states = new LinkedList<>();
	String name;
	String func;
	boolean hidename;
	boolean scripted;
	boolean mouseonly;

	int npcid;
	boolean flip;
	String stance;
	boolean control;

	Random random = new Random();
	NameLabel namelabel;
	NameLabel funclabel;

	InvisibleButton clickRange;
	public Npc(int id, int oid, boolean fl,
			   int fh, boolean cnt,
			   int[] spawnposition) {
        super(oid, spawnposition);
        String strid = StringUtil.extend(id, 7);

		Node src = NXFiles.Npc().subNode(strid + ".img");
		Node strsrc = NXFiles.String().
		subNode("Npc.img").subNode(String.valueOf(id));

		String link = src.subNode("info").
		subNode("link").getString();

		if (link.length() > 0)
		{
			src = NXFiles.Npc().subNode(link+ ".img");
		}

		Node info = src.subNode("info");

		hidename = info.subNode("hideName").getBool();
		mouseonly = info.subNode("talkMouseOnly").getBool();
		scripted = info.subNode("script").nChild() > 0 || info.subNode("shop").getBool();
		int nChild = src.nChild();

		for (int i = 0; i < nChild; i++)
		{
			Node npcnode = src.subNode(i);
			String state = npcnode.getName();

			if (!state.equals("info"))
			{
				animations.put(state, new Animation(npcnode));
				states.add(state);
			}
			Node sp = npcnode.subNode("speak");
			int nChild2 = sp.nChild();
			for (int i2 = 0; i2 < nChild2; i2++){
				Node speaknode = sp.subNode(i2);
				if(lines.containsKey(state))
					lines.get(state).add(strsrc.subNode(speaknode.getString()).getString());
				else{
					LinkedList<String> l = new LinkedList<>();
					l.add(strsrc.subNode(speaknode.getString()).getString());
					lines.put(state, l);
				}
			}
		}

		name = strsrc.subNode("name").getString();
		func = strsrc.subNode("func").getString();

		namelabel = new NameLabel(name, Text.texraFont);
		namelabel.setAlignment(Align.center);
		namelabel.setColor(Color.YELLOW);
		//Text(Text::Font::A13B, Text::Alignment::CENTER, Color::Name::YELLOW, Text::Background::NAMETAG, name);
		funclabel = new NameLabel(func, Text.texraFont);
		//Text(Text::Font::A13B, Text::Alignment::CENTER, Color::Name::YELLOW, Text::Background::NAMETAG, func);
		funclabel.setAlignment(Align.center);
		funclabel.setColor(Color.YELLOW);
		npcid = id;
		flip = !fl;
		control = cnt;
		stance = "stand";

		phobj.fhid = fh;
		setPos(spawnposition[0], spawnposition[1]);
		clickRange = new InvisibleButton(this);
	}
	DrawArg arg = new DrawArg();
	public void draw(double viewx, double viewy, float alpha, SpriteBatch batch)
	{
		int[] absp = phobj.get_absolute(viewx, viewy, alpha);
		arg.pos[0] = absp[0];
		arg.pos[1] = absp[1];
		int[] dim =
			animations.containsKey(stance) ?
			animations.get(stance).getDimension() :
			new int[2];
		clickRange.setBounds(absp[0], absp[1], dim[0], dim[1]);
		arg.setFlip(flip);
		if (animations.containsKey(stance))
			animations.get(stance).draw(arg, alpha, batch);

		if (!hidename)
		{
			// If ever changing code for namelabel confirm placements with map 10000
			namelabel.setPosition(absp[0], absp[1] - 4);
			funclabel.setPosition(absp[0], absp[1] + 18);
			//namelabel.draw(absp + Point<int16_t>(0, -4));
			//funclabel.draw(absp + Point<int16_t>(0, 18));
		}
	}

	public int update( Physics physics)
	{
		if (!active)
			return phobj.fhlayer;
		physics.move(phobj);

		if (animations.containsKey(stance))
		{
			boolean aniend = animations.get(stance).update();

			if (aniend && !states.isEmpty())
			{
				int next_stance = random.nextInt(states.size());
				String new_stance = states.get(next_stance);
				set_stance(new_stance);
			}
		}

		return phobj.fhlayer;
	}

	public void set_stance(String st)
	{
		if (!stance.equals(st))
		{
			stance = st;

			Animation iter = animations.get(stance);

			if (iter == null)
				return;

			iter.reset();
		}
	}

	public boolean isscripted()
	{
		return scripted;
	}

	//	boolean inrange(int[] cursorpos, int[] viewpos)
	//	{
	//		if (!active)
	//			return false;
	//
	//		int[] absp = getPosition();
	//		absp[0] += viewpos[0];
	//		absp[1] += viewpos[1];
	//
	//		int[] dim =
	//			animations.containsKey(stance) ?
	//			animations.get(stance).getDimension() :
	//			new int[2];
	//		int l = 		absp[0] - dim[0] / 2,
	//		r  = absp[0] + dim[0] / 2,
	//	 t =	absp[1] - dim[1],
	//	b=	absp[1];
	//		return !(l == r && t == b) && cursorpos[0]
	//		!straight() &&
	//                v[0] >= left() && v[0] <= right() &&
	//                v[1] >= top() && v[1] <= bottom();
	//		return new Rectangle(
	//			absp[0] - dim[0] / 2,
	//			absp[0] + dim[0] / 2,
	//			absp[1] - dim[1],
	//			absp[1]
	//			).contains(cursorpos);
	//	}

	public String get_name()
	{
		return name;
	}

	public String get_func()
	{
		return func;
	}
}
