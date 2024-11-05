package cn.treeh.Game.Data;

import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.Player.Job.SkillId;
import cn.treeh.Graphics.Rectangle;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxUtils;
import cn.treeh.Util.StringUtil;

import java.util.LinkedList;
import java.util.TreeMap;

public class SkillData {
	static TreeMap<Integer, SkillData> cache = new TreeMap<>();
	public static SkillData getSkillData(int id){
		if(cache.containsKey(id)){
			return cache.get(id);
		}
		SkillData sd = new SkillData(id);
		cache.put(id, sd);
		return sd;
	}
	static public class Stats
			{
				float damage;
				int matk;
				int fixdamage;
				int mastery;
				int attackcount;
				int mobcount;
				int bulletcount;
				int bulletcost;
				int hpcost;
				int mpcost;
				float chance;
				float critical;
				float ignoredef;
				float hrange;
				Rectangle range;

				Stats(float damage, int matk, int fixdamage,
					  int mastery, int attackcount, int mobcount,
					  int bulletcount, int bulletcost, int hpcost,
					  int mpcost, float chance, float critical,
					  float ignoredef, float hrange, Rectangle range){
					this.damage = damage;
					this.matk = matk;
					this.fixdamage = fixdamage;
					this.mastery = mastery;
					this.attackcount=attackcount;
					this.mobcount=mobcount;
					this.bulletcount=bulletcount;
					this.bulletcost=bulletcost;
					this.hpcost=hpcost;
					this.mpcost=mpcost;
					this.chance=chance;
					this.critical=critical;
					this.ignoredef=ignoredef;
					this.hrange=hrange;
					this.range=range;

				}
			}

	// Skill flags, unfortunately these just have to be hard-coded
	enum Flags
			{
				NONE,
				ATTACK,
				RANGED
			}

	// Icon types
	enum Icon
			{
				NORMAL,
				DISABLED,
				MOUSEOVER,
				NUM_ICONS
			}

	// Get some hard-coded information

	TreeMap<Integer, Stats> stats;
	String element;
	Weapon.Type reqweapon;
	int masterlevel;
	int flags;
	boolean passive;
	boolean invisible;

	String name;
	String desc;
	TreeMap<Integer, String> levels = new TreeMap<>();
	TreeMap<Integer, Integer> reqskills = new TreeMap<>();

	Texture[] icons = new Texture[Icon.NUM_ICONS.ordinal()];

	private SkillData(int id)
	{
		/// Locate sources
		String strid = StringUtil.extend(id, 7);
		String jobid = strid.substring(0, 3);
		Node src = NXFiles.Skill().subNode(jobid + ".img").
		subNode("skill").subNode(strid);
		Node strsrc = NXFiles.String().subNode("Skill.img").subNode(strid);

		/// Load icons
		icons[0] = new Texture(src.subNode("icon"));
		icons[1] = new Texture(src.subNode("iconDisabled"));
		icons[2] = new Texture(src.subNode("iconMouseOver"));
		/// Load strings
		name = strsrc.subNode("name").getString();
		desc = strsrc.subNode("desc").getString();

		for (int level = 1; ; level++){
			Node sub = strsrc.subNode("h" + level);
			if(!sub.valid())
				break;
			levels.put(level, sub.getString());
		}



		/// Load stats
		Node levelsrc = src.subNode("level");
		int nChild = levelsrc.nChild();
		for(int i = 0; i < nChild; i++){
			Node sub = levelsrc.subNode(i);
			float damage = (float)sub.subNode("damage").getReal() / 100;
			int matk = sub.subNode("mad").getInt();
			int fixdamage = sub.subNode("fixdamage").getInt();
			int mastery = sub.subNode("mastery").getInt();
			int attackcount = (int)sub.subNode("attackCount").getInt(1);
			int mobcount = (int)sub.subNode("mobCount").getInt(1);
			int bulletcount = (int)sub.subNode("bulletCount").getInt(1);
			int bulletcost = (int)sub.subNode("bulletConsume").getInt(bulletcount);
			int hpcost = sub.subNode("hpCon").getInt();
			int mpcost = sub.subNode("mpCon").getInt();
			float chance = (float) (sub.subNode("prop").getReal(100.0) / 100f);
			float critical = 0.0f;
			float ignoredef = 0.0f;
			float hrange = (float)(sub.subNode("range").getReal(100.0) / 100f);
			Rectangle range = new Rectangle(sub);
			int level = -1;
			try{
				level = Integer.parseInt(sub.getName());
			} catch (NumberFormatException ignore) {
			}


			stats.put(level,
					  new Stats(damage, matk, fixdamage, mastery, attackcount,
								mobcount, bulletcount, bulletcost, hpcost, mpcost,
								chance, critical, ignoredef, hrange, range)
			);
		}

		element = src.subNode("elemAttr").getString();

		if (jobid.equals("900") || jobid.equals("910"))
			reqweapon = Weapon.Type.NONE;
		else
			reqweapon = Weapon.byId(100 + src.subNode("weapon").getInt());

		masterlevel = stats.size();
		passive = (id % 10000) / 1000 == 0;
		flags = flags_of(id);
		invisible = src.subNode("invisible").getBool();

		/// Load required skills
		Node reqsrc = src.subNode("req");
		nChild = reqsrc.nChild();
		for (int i = 0; i < nChild; i++)
		{
			Node sub = reqsrc.subNode(i);
			int skillid = -1;
			try{
				skillid = Integer.parseInt(sub.getName());
			} catch (NumberFormatException ignore) {
			}
			int reqlv = sub.getInt();

			reqskills.put(skillid, reqlv);
		}
	}
	static TreeMap<Integer, Integer> skill_flags = new TreeMap<Integer, Integer>(){{
		// Beginner
		put(SkillId.Id.THREE_SNAILS.id, Flags.ATTACK.ordinal());
		// Warrior
		put(SkillId.Id.POWER_STRIKE.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.SLASH_BLAST.id, Flags.ATTACK.ordinal());
		// Fighter
		// Page
		// Crusader
		put(SkillId.Id.SWORD_PANIC.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.AXE_PANIC.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.SWORD_COMA.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.AXE_COMA.id, Flags.ATTACK.ordinal());
		// Hero
		put(SkillId.Id.RUSH_HERO.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.BRANDISH.id, Flags.ATTACK.ordinal());
		// Page
		// White Knight
		put(SkillId.Id.CHARGE.id, Flags.ATTACK.ordinal());
		// Paladin
		put(SkillId.Id.RUSH_PALADIN.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.BLAST.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.HEAVENS_HAMMER.id, Flags.ATTACK.ordinal());
		// Spearman
		// Dragon Knight
		put(SkillId.Id.DRAGON_BUSTER.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.DRAGON_FURY.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.PA_BUSTER.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.PA_FURY.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.SACRIFICE.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.DRAGONS_ROAR.id, Flags.ATTACK.ordinal());
		// Dark Knight
		put(SkillId.Id.RUSH_DK.id, Flags.ATTACK.ordinal());
		// Mage
		put(SkillId.Id.ENERGY_BOLT.id, Flags.ATTACK.ordinal() | Flags.RANGED.ordinal());
		put(SkillId.Id.MAGIC_CLAW.id, Flags.ATTACK.ordinal() | Flags.RANGED.ordinal());
		// F/P Mage
		put(SkillId.Id.SLOW_FP.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.FIRE_ARROW.id, Flags.ATTACK.ordinal() | Flags.RANGED.ordinal());
		put(SkillId.Id.POISON_BREATH.id, Flags.ATTACK.ordinal() | Flags.RANGED.ordinal());
		// F/P ArchMage
		put(SkillId.Id.EXPLOSION.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.POISON_BREATH.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.SEAL_FP.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.ELEMENT_COMPOSITION_FP.id, Flags.ATTACK.ordinal() | Flags.RANGED.ordinal());
		// TODO: Blank?
		put(SkillId.Id.FIRE_DEMON.id, Flags.ATTACK.ordinal());
		put(SkillId.Id.PARALYZE.id, Flags.ATTACK.ordinal() | Flags.RANGED.ordinal());
		put(SkillId.Id.METEOR_SHOWER.id, Flags.ATTACK.ordinal());
	}};
	int flags_of(int id)
	{


		int iter = skill_flags.getOrDefault(id, -1);

		if (iter == -1)
			return Flags.NONE.ordinal();

		return iter;
	}

	public boolean is_passive()
	{
		return passive;
	}

	boolean is_attack()
	{
		return !passive && (flags & Flags.ATTACK.ordinal()) != 0;
	}

	boolean is_invisible()
	{
		return invisible;
	}

	int get_masterlevel()
	{
		return masterlevel;
	}

	Weapon.Type get_required_weapon()
	{
		return reqweapon;
	}
	static Stats null_stats = new Stats(0.0f, 0, 0, 0, 0, 0, 0, 0, 0,
										0, 0.0f, 0.0f, 0.0f, 0.0f,
										new Rectangle(0,0,0,0));
	SkillData.Stats get_stats(int level)
	{
		Stats iter = stats.get(level);

		if (iter == null)
		{

			return null_stats;
		}

		return iter;
	}
	String get_name()
	{
		return name;
	}

	String get_desc()
	{
		return desc;
	}
	static String null_level = "Missing level description.";
	String get_level_desc(int level)
	{
		String iter = levels.get(level);

		if (iter == null)
		{


			return null_level;
		}
		else
		{
			return iter;
		}
	}

	Texture get_icon(Icon icon)
	{
		return icons[icon.ordinal()];
	}

	public TreeMap<Integer,  Integer> get_reqskills()
	{
		return reqskills;
	}
}