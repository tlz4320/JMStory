package cn.treeh.IO.Key;

public enum KeyType {
    NONE,
    SKILL,
    ITEM,
    CASH,
    MENU,
    ACTION,
    FACE,
    NONE2,
    MACRO,
    TEXT,
    LENGTH;

    public static KeyType getId(int id) {
        if (id <= 0 || id >= 9)
            return KeyType.NONE;

        return KeyType.values()[id];
    }
}
