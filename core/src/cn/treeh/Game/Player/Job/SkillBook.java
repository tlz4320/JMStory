package cn.treeh.Game.Player.Job;

import cn.treeh.Game.Data.SkillData;

import java.util.Map;
import java.util.TreeMap;

public class SkillBook {
    public static TreeMap<Integer, SkillEntry> skillentries = new TreeMap<>();
    public void set_skill(int id, int level, int mlevel, long expire)
	{
        skillentries.put(id,
                         new SkillEntry(level, mlevel, expire));
	}

	boolean has_skill(int id)
	{
		return skillentries.containsKey(id);
	}

	public int get_level(int id)
	{
		SkillEntry iter = skillentries.get(id);

		if (iter == null)
			return 0;

		return iter.level;
	}

	int get_masterlevel(int id)
	{
		SkillEntry iter = skillentries.get(id);

		if (iter == null)
			return 0;
		return iter.masterlevel;
	}

	long get_expiration(int id)
	{
		SkillEntry iter = skillentries.get(id);

		if (iter == null)
			return 0;

		return iter.expiration;
	}

	public Map<Integer, Integer> collect_passives()
	{
		TreeMap<Integer, Integer> passives = new TreeMap<>();

		for (Map.Entry<Integer, SkillEntry> iter : skillentries.entrySet())
			if (SkillData.getSkillData(iter.getKey()).is_passive())
				passives.put(iter.getKey(), iter.getValue().level);

		return passives;
	}

	TreeMap<Integer, Integer> collect_required(int id)
	{
		SkillEntry iter = skillentries.get(id);

		if (iter == null)
			return new TreeMap<>();

		return SkillData.getSkillData(id).get_reqskills();
	}

}