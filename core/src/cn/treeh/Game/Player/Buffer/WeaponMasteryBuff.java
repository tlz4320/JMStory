package cn.treeh.Game.Player.Buffer;

import cn.treeh.Game.Inventory.EquipStat;
import cn.treeh.Game.Inventory.Weapon;
import cn.treeh.Game.Player.CharStat;
import cn.treeh.NX.Node;

public class WeaponMasteryBuff extends PassiveBuff{
    Weapon.Type w1, w2;
    int counts = 0;
    //因为C++支持多参数的模板函数，Java没有，所以需要一个Counts来判断初始化参数数量
    //个人认为这样并不会产生额外的计算
    public WeaponMasteryBuff(Weapon.Type t1){
        w1 = t1;
        counts = 1;
    }
    public WeaponMasteryBuff(Weapon.Type t1, Weapon.Type t2){
        w1 = t1;
        w2 = t2;
        counts = 2;
    }
    @Override
    boolean is_applicable(CharStat stats, Node level) {
        if (counts == 1) {
            return (stats.get_weapontype() == w1);
        }
        return stats.get_weapontype() == w1 || stats.get_weapontype() == w2;
    }

    @Override
    void apply_to(CharStat stats, Node level) {
        float mastery = level.subNode("mastery").getInt() / 100f;
		stats.set_mastery(mastery);
        stats.add_value(EquipStat.Id.ACC, level.subNode("x").getInt());
    }
}