package cn.treeh.NX;

import java.util.HashMap;

public class NXFiles {
    static NxFile Item, Audio, Mob, UI, Map, Sound, String;
    static void init(){
        Item = new NxFile("Item.nx");
        String = new NxFile("String.nx");

    }
    public static Node Item(){
        return Item.getNode();
    }
    public static Node String(){
        return String.getNode();
    }
}
