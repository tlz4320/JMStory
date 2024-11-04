package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.Weapon;

import java.util.HashMap;

public class PassiveBuffs {
    static HashMap<Integer, PassiveBuff> buffs = new HashMap<>();
    public PassiveBuffs(){
        //TODO 飞侠和弓箭手的精通技能是没有的 目前代码只有战士部分
        buffs.put(SkillId.Id.ANGEL_BLESSING, new AngelBlessingBuff());

		// Fighter
        buffs.put(SkillId.Id.SWORD_MASTERY_FIGHTER, new WeaponMasteryBuff(Weapon.Type.SWORD_1H, Weapon.Type.SWORD_2H));
        buffs.put(SkillId.Id.AXE_MASTERY, new WeaponMasteryBuff(Weapon.Type.AXE_1H, Weapon.Type.AXE_2H));

		// Crusader

		// Hero
        buffs.put(SkillId.Id.ACHILLES_HERO, new AchillesBuff());

		// Page
        buffs.put(SkillId.Id.SWORD_MASTERY_FIGHTER, new WeaponMasteryBuff(Weapon.Type.SWORD_1H, Weapon.Type.SWORD_2H));
        buffs.put(SkillId.Id.BW_MASTERY, new WeaponMasteryBuff(Weapon.Type.MACE_1H, Weapon.Type.MACE_2H));

		// White Knight

		// Paladin
        buffs.put(SkillId.Id.ACHILLES_PALADIN, new AchillesBuff());

		// Spearman
        buffs.put(SkillId.Id.SPEAR_MASTERY, new WeaponMasteryBuff(Weapon.Type.SPEAR));
        buffs.put(SkillId.Id.PA_MASTERY, new WeaponMasteryBuff(Weapon.Type.POLEARM));

		// Dragon Knight

		// Dark Knight
        buffs.put(SkillId.Id.ACHILLES_DK, new AchillesBuff());
        buffs.put(SkillId.Id.BERSERK, new BerserkBuff());
    }
}