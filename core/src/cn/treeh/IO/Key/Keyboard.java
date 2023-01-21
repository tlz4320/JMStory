package cn.treeh.IO.Key;

import com.badlogic.gdx.Input;

import java.util.HashMap;

public class Keyboard {
    public enum Id
    {
        EQUIPMENT,
                ITEMS,
                STATS,
                SKILLS,
                FRIENDS,
                WORLDMAP,
                MAPLECHAT,
                MINIMAP,
                QUESTLOG,
                KEYBINDINGS,
                SAY,
                WHISPER,
                PARTYCHAT,
                FRIENDSCHAT,
                MENU,
                QUICKSLOTS,
                TOGGLECHAT,
                GUILD,
                GUILDCHAT,
                PARTY,
                NOTIFIER,
                MAPLENEWS,			// TOSPOUSE (v83)
                CASHSHOP,			// MONSTERBOOK (v83)
                ALLIANCECHAT,		// CASHSHOP (v83)
                NONE,		// TOALLIANCE (v83)
                MANAGELEGION,	// PARTYSEARCH (v83)
                MEDALS,				// FAMILY (v83)
                BOSSPARTY,			// MEDAL (v83)
                NONE2,
                PROFESSION,
                ITEMPOT,
                EVENT,
                NONE3,
                SILENTCRUSADE,
                BITS,
                BATTLEANALYSIS,
        NONE4,
        NONE5,
                NONE6,
                GUIDE,
                VIEWERSCHAT,
                ENHANCEEQUIP,
                MONSTERCOLLECTION,
                SOULWEAPON,
                CHARINFO,
                CHANGECHANNEL,
                MAINMENU,
                SCREENSHOT,
                PICTUREMODE,
                MAPLEACHIEVEMENT,
                PICKUP,
                SIT,
                ATTACK,
                JUMP,
                INTERACT_HARVEST,
                FACE1,
                FACE2,
                FACE3,
                FACE4,
                FACE5,
                FACE6,
                FACE7,
                MAPLESTORAGE,
                SAFEMODE,
                MUTE,
                MAPLERELAY,
                FAMILIAR,
                TOSPOUSE,
                // Static keys
                LEFT,
                RIGHT,
                UP,
                DOWN,
                BACK,
                TAB,
                RETURN,
                ESCAPE,
                SPACE,
                DELETE,
                HOME,
                END,
                COPY,
                PASTE,
                LENGTH
    }
    public static HashMap<Integer, KeyMapping> keymap = new HashMap<>();
    static {
        keymap.put(Input.Keys.LEFT, new KeyMapping(KeyType.ACTION, Id.LEFT.ordinal()));
        keymap.put(Input.Keys.RIGHT,new KeyMapping(KeyType.ACTION, Id.RIGHT.ordinal()));
        keymap.put(Input.Keys.UP,new KeyMapping(KeyType.ACTION, Id.UP.ordinal()));
        keymap.put(Input.Keys.DOWN,new KeyMapping(KeyType.ACTION, Id.DOWN.ordinal()));
        keymap.put(Input.Keys.ENTER,new KeyMapping(KeyType.ACTION, Id.RETURN.ordinal()));
        keymap.put(Input.Keys.NUMPAD_ENTER,new KeyMapping(KeyType.ACTION, Id.RETURN.ordinal()));
        keymap.put(Input.Keys.TAB,new KeyMapping(KeyType.ACTION, Id.TAB.ordinal()));
    }
    public static void assign(int key, int tid, int action){
        KeyType type = KeyType.getId(tid);
        if(type != KeyType.NONE){
            keymap.put(key, new KeyMapping(type, action));
        }
    }
    public static void remove(int key){
        keymap.put(key, none);
    }
    public static Id getCtrlAction(int keycode)
    {

        switch (keycode)
        {
            case Input.Keys.C:
                return Id.COPY;
            case Input.Keys.V:
                return Id.PASTE;
            default:
                return Id.LENGTH;
        }
    }
    public static Id getAction(int keycode)
    {
        return Id.values()[keycode];
    }
    static KeyMapping none = new KeyMapping(KeyType.NONE, 0);
    public static KeyMapping getMapping(int key){
        return keymap.getOrDefault(key, none);
    }
}
