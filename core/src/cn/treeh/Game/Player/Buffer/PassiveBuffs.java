package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.Player.CharStat;
import cn.treeh.Game.Player.Job.SkillId;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.StringUtil;

import java.util.HashMap;

public class PassiveBuffs {
    static HashMap<Integer, PassiveBuff> buffs = new HashMap<>();
    public PassiveBuffs(){
        //TODO 飞侠和弓箭手的精通技能是没有的 目前代码只有战士部分
        buffs.put(SkillId.Id.ANGEL_BLESSING.id, new AngelBlessingBuff());

		// Fighter
        buffs.put(SkillId.Id.SWORD_MASTERY_FIGHTER.id, new WeaponMasteryBuff(Weapon.Type.SWORD_1H, Weapon.Type.SWORD_2H));
        buffs.put(SkillId.Id.AXE_MASTERY.id, new WeaponMasteryBuff(Weapon.Type.AXE_1H, Weapon.Type.AXE_2H));

		// Crusader

		// Hero
        buffs.put(SkillId.Id.ACHILLES_HERO.id, new AchillesBuff());

		// Page
        buffs.put(SkillId.Id.SWORD_MASTERY_FIGHTER.id, new WeaponMasteryBuff(Weapon.Type.SWORD_1H, Weapon.Type.SWORD_2H));
        buffs.put(SkillId.Id.BW_MASTERY.id, new WeaponMasteryBuff(Weapon.Type.MACE_1H, Weapon.Type.MACE_2H));

		// White Knight

		// Paladin
        buffs.put(SkillId.Id.ACHILLES_PALADIN.id, new AchillesBuff());

		// Spearman
        buffs.put(SkillId.Id.SPEAR_MASTERY.id, new WeaponMasteryBuff(Weapon.Type.SPEAR));
        buffs.put(SkillId.Id.PA_MASTERY.id, new WeaponMasteryBuff(Weapon.Type.POLEARM));

		// Dragon Knight

		// Dark Knight
        buffs.put(SkillId.Id.ACHILLES_DK.id, new AchillesBuff());
        buffs.put(SkillId.Id.BERSERK.id, new BerserkBuff());
    }
    public void apply_buff(CharStat stats, int skill_id, int skill_level)
	{
        PassiveBuff iter = buffs.get(skill_id);

		if (iter == null)
			return;

		boolean wrong_job = !stats.get_job().can_use(skill_id);

		if (wrong_job)
			return;

	   String strid;

		if (skill_id < 10000000)
			strid = StringUtil.extend(skill_id, 7);
		else
			strid = "" + skill_id;

        Node src = NXFiles.Skill().subNode(strid.substring(0, 3) + ".img").subNode("skill").
            subNode(strid).subNode("level").subNode(skill_level);
		if (iter.is_applicable(stats, src))
            iter.apply_to(stats, src);
	}
}