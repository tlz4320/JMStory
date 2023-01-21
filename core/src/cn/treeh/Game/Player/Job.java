package cn.treeh.Game.Player;

import cn.treeh.Game.Inventory.Weapon;

public class Job {
    public enum Level {
        BEGINNER,
        FIRST,
        SECOND,
        THIRD,
        FOURTHT
    }
    public static Level get_next_level(Level level)
    {
        switch (level)
        {
            case BEGINNER:
                return Level.FIRST;
            case FIRST:
                return Level.SECOND;
            case SECOND:
                return Level.THIRD;
            default:
                return Level.FOURTHT;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    String name;
    int id;
    Level level;
    public int getSubJob(Level lv)
    {
        if (lv.ordinal() <= level.ordinal())
        {
            switch (lv)
            {
                case BEGINNER:
                    return 0;
                case FIRST:
                    return (id / 100) * 100;
                case SECOND:
                    return (id / 10) * 10;
                case THIRD:
                    return (level == Level.FOURTHT) ? id - 1 : id;
                case FOURTHT:
                    return id;
            }
        }
        return 0;
    }
    public String getName(int jid)
    {
        switch (jid)
        {
            case 0:
                return "Beginner";
            case 100:
                return "Swordsman";
            case 110:
                return "Fighter";
            case 111:
                return "Crusader";
            case 112:
                return "Hero";
            case 120:
                return "Page";
            case 121:
                return "White Knight";
            case 122:
                return "Paladin";
            case 130:
                return "Spearman";
            case 131:
                return "Dragon Knight";
            case 132:
                return "Dark Knight";
            case 200:
                return "Magician";
            case 210:
                return "Wizard (F/P)";
            case 211:
                return "Mage (F/P)";
            case 212:
                return "Archmage (F/P)";
            case 220:
                return "Wizard (I/L)";
            case 221:
                return "Mage (I/L)";
            case 222:
                return "Archmage (I/L)";
            case 230:
                return "Cleric";
            case 231:
                return "Priest";
            case 232:
                return "Bishop";
            case 300:
                return "Archer";
            case 310:
                return "Hunter";
            case 311:
                return "Ranger";
            case 312:
                return "Bowmaster";
            case 320:
                return "Crossbowman";
            case 321:
                return "Sniper";
            case 322:
                return "Marksman";
            case 400:
                return "Rogue";
            case 410:
                return "Assassin";
            case 411:
                return "Hermit";
            case 412:
                return "Nightlord";
            case 420:
                return "Bandit";
            case 421:
                return "Chief Bandit";
            case 422:
                return "Shadower";
            case 500:
                return "Pirate";
            case 510:
                return "Brawler";
            case 511:
                return "Marauder";
            case 512:
                return "Buccaneer";
            case 520:
                return "Gunslinger";
            case 521:
                return "Outlaw";
            case 522:
                return "Corsair";
            case 2000:
            case 2100:
            case 2110:
            case 2111:
            case 2112:
                return "Aran";
            default:
                return "";
        }
    }
    public EquipStat.Id getPrimary(Weapon.Type weapontype)
    {
        switch (id / 100)
        {
            case 2:
                return EquipStat.Id.INT;
            case 3:
                return EquipStat.Id.DEX;
            case 4:
                return EquipStat.Id.LUK;
            case 5:
                return (weapontype == Weapon.Type.GUN) ? EquipStat.Id.DEX : EquipStat.Id.STR;
            default:
                return EquipStat.Id.STR;
        }
    }

    public EquipStat.Id getSecond(Weapon.Type weapontype)
    {
        switch (id / 100)
        {
            case 2:
                return EquipStat.Id.LUK;
            case 3:
                return EquipStat.Id.STR;
            case 5:
                return (weapontype == Weapon.Type.GUN) ? EquipStat.Id.STR : EquipStat.Id.DEX;
            default:
                return EquipStat.Id.DEX;
        }
    }
}
