package cn.treeh.Game.Player;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.Player.Job.Job;
import cn.treeh.Graphics.Rectangle;
import cn.treeh.IO.Net.StatsEntry;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class CharStat {
	String name;
	LinkedList<Long> petids;
	Job job;
	long exp;
	int mapid;
	int portal;
	Map.Entry<Integer, Integer> rank;
	Map.Entry<Integer, Integer> jobrank;
	HashMap<MapleStat.Id, Integer> basestats = new HashMap<>();
	HashMap<EquipStat.Id, Integer> totalstats = new HashMap<>();
	HashMap<EquipStat.Id, Integer> buffdeltas = new HashMap<>();
	HashMap<EquipStat.Id, Float> percentages = new HashMap<>();
	int maxdamage;
	int mindamage;
	int honor;
	int attackspeed;
	int projectilerange;
	Weapon.Type weapontype;
	float mastery;
	float critical;
	float mincrit;
	float maxcrit;
	float damagepercent;
	float bossdmg;
	float ignoredef;
	float stance;
	float resiststatus;
	float reducedamage;
	boolean female;
	public int get_total(EquipStat.Id stat){
		return totalstats.getOrDefault(stat, 0);
	}
	CharStat(StatsEntry s){
		name = s.name;
		petids = s.petids;
		exp = s.exp;
		mapid = s.mapid;
		portal = s.portal;
		rank = s.rank;
		jobrank = s.jobrank;
		basestats = s.stats;
		female = s.female;
		job = new Job(basestats.get(MapleStat.Id.JOB));
		init_totalstats();
	}

	void init_totalstats()
	{
		totalstats.clear();
		buffdeltas.clear();
		percentages.clear();

		totalstats.put(EquipStat.Id.HP, get_stat(MapleStat.Id.MAXHP));
		totalstats.put(EquipStat.Id.MP, get_stat(MapleStat.Id.MAXMP));
		totalstats.put(EquipStat.Id.STR, get_stat(MapleStat.Id.STR));
		totalstats.put(EquipStat.Id.DEX, get_stat(MapleStat.Id.DEX));
		totalstats.put(EquipStat.Id.INT, get_stat(MapleStat.Id.INT));
		totalstats.put(EquipStat.Id.LUK, get_stat(MapleStat.Id.LUK));
		totalstats.put(EquipStat.Id.SPEED, 100);
		totalstats.put(EquipStat.Id.JUMP, 100);

		maxdamage = 0;
		mindamage = 0;
		honor = 0;
		attackspeed = 0;
		projectilerange = 400;
		mastery = 0.0f;
		critical = 0.05f;
		mincrit = 0.5f;
		maxcrit = 0.75f;
		damagepercent = 0.0f;
		bossdmg = 0.0f;
		ignoredef = 0.0f;
		stance = 0.0f;
		resiststatus = 0.0f;
		reducedamage = 0.0f;
	}
	void close_totalstats()
	{
		totalstats.put(EquipStat.Id.ACC, totalstats.getOrDefault(EquipStat.Id.ACC, 0) + calculateaccuracy());


		for (Map.Entry<EquipStat.Id, Float> iter : percentages.entrySet())
		{
			EquipStat.Id stat = iter.getKey();
			int total = totalstats.get(stat);
			total += (int) (total * iter.getValue());
			set_total(stat, total);
		}

		int primary = get_primary_stat();
		int secondary = get_secondary_stat();
		int attack = get_total(EquipStat.Id.WATK);
		float multiplier = damagepercent + (attack / 100f);
		maxdamage = (int)((primary + secondary) * multiplier);
		mindamage = (int)(((primary * 0.9f * mastery) + secondary) * multiplier);
	}

	int calculateaccuracy()
	{
		int totaldex = get_total(EquipStat.Id.DEX);
		int totalluk = get_total(EquipStat.Id.LUK);

		return (int)(totaldex * 0.8f + totalluk * 0.5f);
	}

	int get_primary_stat()
	{
		EquipStat.Id primary = job.getPrimary(weapontype);

		return (int)(get_multiplier() * get_total(primary));
	}

	int get_secondary_stat()
	{
		EquipStat.Id secondary = job.getSecond(weapontype);

		return get_total(secondary);
	}

	float get_multiplier()
	{
		switch (weapontype)
		{
			case SWORD_1H:
				return 4.0f;
				case AXE_1H:
					case MACE_1H:
						case WAND:
							case STAFF:
								return 4.4f;
								case DAGGER:
									case CROSSBOW:
										case CLAW:
											case GUN:
												return 3.6f;
												case SWORD_2H:
													return 4.6f;
													case AXE_2H:
														case MACE_2H:
															case KNUCKLE:
																return 4.8f;
																case SPEAR:
																	case POLEARM:
																		return 5.0f;
																		case BOW:
																			return 3.4f;
																			default:
																				return 0.0f;
		}
	}

	void set_stat(MapleStat.Id stat, int value)
	{
		basestats.put(stat, value);
	}
	static HashMap<EquipStat.Id, Integer> EQSTAT_CAPS = new HashMap<>(){
		{
			put(EquipStat.Id.STR,	999	);
			put(EquipStat.Id.DEX,	999	);
			put(EquipStat.Id.INT,	999	);
			put(EquipStat.Id.LUK,	999	);
			put(EquipStat.Id.HP,	30000);
			put(EquipStat.Id.MP,	30000);
			put(EquipStat.Id.WATK,	999	);
			put(EquipStat.Id.MAGIC,	2000);
			put(EquipStat.Id.WDEF,	999	);
			put(EquipStat.Id.MDEF,	999	);
			put(EquipStat.Id.ACC,	999	);
			put(EquipStat.Id.AVOID,	999	);
			put(EquipStat.Id.HANDS,	999	);
			put(EquipStat.Id.SPEED,	140	);
			put(EquipStat.Id.JUMP,	123	);
		}};
	void set_total(EquipStat.Id stat, int value)
	{
		int cap_value = EQSTAT_CAPS.getOrDefault(stat, -1);

		if (cap_value != -1)
		{

			if (value > cap_value)
				value = cap_value;
		}

		totalstats.put(stat, value);
	}

	public void add_buff(EquipStat.Id stat, int value)
	{
		int current = get_total(stat);
		set_total(stat, current + value);
		buffdeltas.put(stat,
					   buffdeltas.getOrDefault(stat, 0) + value);
	}

	public void add_value(EquipStat.Id stat, int value)
	{
		int current = get_total(stat);
		set_total(stat, current + value);
	}

	public void add_percent(EquipStat.Id stat, float percent)
	{
		percentages.put(stat,
						percentages.getOrDefault(stat, 0f) + percent);
	}

	void set_weapontype(Weapon.Type w)
	{
		weapontype = w;
	}

	void set_exp(long e)
	{
		exp = e;
	}

	void set_portal(int p)
	{
		portal = p;
	}

	public void set_mastery(float m)
	{
		mastery = 0.5f + m;
	}

	public void set_damagepercent(float d)
	{
		damagepercent = d;
	}

	public void set_reducedamage(float r)
	{
		reducedamage = r;
	}

	void change_job(int id)
	{
		basestats.put(MapleStat.Id.JOB, id);
		job.change_job(id);
	}

	int calculate_damage(int mobatk)
	{
		// TODO: Random stuff, need to find the actual formula somewhere.
		int weapon_def = get_total(EquipStat.Id.WDEF);

		if (weapon_def == 0)
			return mobatk;

		int reduceatk = mobatk / 2 + mobatk / weapon_def;

		return reduceatk - (int)(reduceatk * reducedamage);
	}

	boolean is_damage_buffed()
	{
		return get_buffdelta(EquipStat.Id.WATK) > 0 || get_buffdelta(EquipStat.Id.MAGIC) > 0;
	}

	public int get_stat(MapleStat.Id stat)
	{
		return basestats.get(stat);
	}

	int get_buffdelta(EquipStat.Id stat)
	{
		return buffdeltas.get(stat);
	}
	Rectangle attackRect = null;
	Rectangle get_range()
	{
		if(attackRect == null){
			attackRect = new Rectangle(-projectilerange, -5, -50, 50);
		}
		return attackRect;
	}

	void set_mapid(int id)
	{
		mapid = id;
	}

	int get_mapid()
	{
		return mapid;
	}

	int get_portal()
	{
		return portal;
	}

	long get_exp()
	{
		return exp;
	}

	String get_name()
	{
		return name;
	}

	String get_jobname()
	{
		return job.getName();
	}

	public Weapon.Type get_weapontype()
	{
		return weapontype;
	}

	float get_mastery()
	{
		return mastery;
	}

	float get_critical()
	{
		return critical;
	}

	float get_mincrit()
	{
		return mincrit;
	}

	float get_maxcrit()
	{
		return maxcrit;
	}

	float get_reducedamage()
	{
		return reducedamage;
	}

	float get_bossdmg()
	{
		return bossdmg;
	}

	float get_ignoredef()
	{
		return ignoredef;
	}

	public void set_stance(float s)
	{
		stance = s;
	}

	float get_stance()
	{
		return stance;
	}

	float get_resistance()
	{
		return resiststatus;
	}

	int get_maxdamage()
	{
		return maxdamage;
	}

	int get_mindamage()
	{
		return mindamage;
	}

	int get_honor()
	{
		return honor;
	}

	public void set_attackspeed(int as)
	{
		attackspeed = as;
	}

	int get_attackspeed()
	{
		return attackspeed;
	}

	Job get_job()
	{
		return job;
	}

	boolean get_female()
	{
		return female;
	}
}
