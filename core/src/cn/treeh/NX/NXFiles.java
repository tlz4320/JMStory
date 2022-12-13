package cn.treeh.NX;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;

public class NXFiles {
    static private NxFile Item, Audio, Mob, UI, Map,
            String, Skill, Quest, Reactor, Npc;
    static{
        Item = new NxFile(Gdx.files.internal("Item.nx").file());
        String = new NxFile(Gdx.files.internal("String.nx").file());
        Audio = new NxFile(Gdx.files.internal("Sound.nx").file());
        UI = new NxFile(Gdx.files.internal("UI.nx").file());
        Map = new NxFile(Gdx.files.internal("Map.nx").file());
//        Skill = new NxFile(Gdx.files.internal("Skill.nx").file());
//        Mob = new NxFile(Gdx.files.internal("Mob.nx").file());
//        Quest = new NxFile(Gdx.files.internal("Quest.nx").file());
//        Reactor = new NxFile(Gdx.files.internal("Reactor.nx").file());
//        Npc = new NxFile(Gdx.files.internal("Npc.nx").file());
    }
    public static Node Item(){
        return Item.getNode();
    }
    public static Node Audio(){
        return Audio.getNode();
    }
    public static Node Mob(){
        return Mob.getNode();
    }
    public static Node UI(){
        return UI.getNode();
    }
    public static Node Map(){
        return Map.getNode();
    }
    public static Node Skill(){
        return Skill.getNode();
    }
    public static Node String(){
        return String.getNode();
    }
    public static Node Reactor(){
        return Reactor.getNode();
    }
    public static Node Quest(){
        return Quest.getNode();
    }
    public static Node Npc(){
        return Npc.getNode();
    }
}
