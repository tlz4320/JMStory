package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Player.CharStat;

import java.util.HashMap;

public class ActiveBuffs {
	static HashMap<BuffStat.Id, ActiveBuff> buffs = new HashMap<>();
	public ActiveBuffs(){
		buffs.put(BuffStat.Id.MAPLE_WARRIOR, new MapleWarriorBuff());
		buffs.put(BuffStat.Id.STANCE, new StanceBuff());
		buffs.put(BuffStat.Id.BOOSTER, new BoosterBuff());
		buffs.put(BuffStat.Id.WATK, new SimpleStatBuff(EquipStat.Id.WATK));
		buffs.put(BuffStat.Id.WDEF, new SimpleStatBuff(EquipStat.Id.WDEF));
		buffs.put(BuffStat.Id.MATK, new SimpleStatBuff(EquipStat.Id.MAGIC));
		buffs.put(BuffStat.Id.MDEF, new SimpleStatBuff(EquipStat.Id.MDEF));
		buffs.put(BuffStat.Id.SPEED, new SimpleStatBuff(EquipStat.Id.SPEED));
		buffs.put(BuffStat.Id.JUMP, new SimpleStatBuff(EquipStat.Id.JUMP));
		buffs.put(BuffStat.Id.HYPERBODYHP, new PercentageStatBuff(EquipStat.Id.HP));
		buffs.put(BuffStat.Id.HYPERBODYMP, new PercentageStatBuff(EquipStat.Id.MP));
	}
	// Return the buff effect associated with the buff stat
	public void apply_buff(CharStat stats, BuffStat.Id stat, int value){
		ActiveBuff	buff = buffs.get(stat);
		if ( buff != null)
			buff.apply_to(stats, value);
	}
}