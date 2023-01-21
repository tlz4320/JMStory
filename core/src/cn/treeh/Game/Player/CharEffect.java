package cn.treeh.Game.Player;

import java.util.HashMap;

public class CharEffect {
    public enum Id
    {
        LEVELUP,
        JOBCHANGE,
        SCROLL_SUCCESS,
        SCROLL_FAILURE,
        MONSTER_CARD,
        LENGTH
    };

    static public HashMap<Id, String> PATHS;
    static {
        PATHS = new HashMap<>();
        PATHS.put(Id.LEVELUP, "LevelUp");
        PATHS.put(Id.JOBCHANGE, "JobChanged");
        PATHS.put(Id.SCROLL_SUCCESS, "Enchant/Success");
        PATHS.put(Id.SCROLL_FAILURE, "Enchant/Failure");
        PATHS.put(Id.MONSTER_CARD, "MonsterBook/cardGet");
    }
}
