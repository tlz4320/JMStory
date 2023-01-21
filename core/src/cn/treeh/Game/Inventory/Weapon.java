package cn.treeh.Game.Inventory;

public class Weapon {
    public enum Type
    {
        NONE(0),
        SWORD_1H(130),
        AXE_1H(131),
        MACE_1H(132),
        DAGGER(133),
        WAND(137),
        STAFF(138),
        SWORD_2H(140),
        AXE_2H(141),
        MACE_2H(142),
        SPEAR(143),
        POLEARM(144),
        BOW(145),
        CROSSBOW(146),
        CLAW(147),
        KNUCKLE(148),
        GUN(149),
        CASH(170);
        public int id;
        Type(int id){
            this.id = id;
        }
    }
    static Type[] ids = new Type[171];
    static{
        for(Type type : Type.values())
            ids[type.id] = type;
    }
    public static Type byId(int value){
        if (value < 130 || (value > 133 && value < 137) || value == 139 || (value > 149 && value < 170) || value > 170)
        {
            return Type.NONE;
        }
        return ids[value];
    }
}
