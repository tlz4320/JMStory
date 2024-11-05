package cn.treeh.Audio;

import cn.treeh.Audio.util.BitFileHandle;
import cn.treeh.NX.Audio;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

import java.util.HashMap;

public class SoundPlayer {
    static HashMap<String, Sound> sounds = new HashMap<>();
    public static void addSound(String name, String path){
        if(sounds.containsKey(name))
            return;
        Node node = NXFiles.Audio();
        Node n = node.subNode(path);
        Sound sound = Gdx.audio.newSound(new BitFileHandle(n.getAudio().data(82)));
        sounds.put(name, sound);
    }
    public static Sound getSound(String path){
        return getSound(path, path);
    }
    public static Sound getSound(Node n){
        Audio a = n.getAudio();
        if(sounds.containsKey(a.idStr())){
            return sounds.get(a.idStr());
        } else{
            Sound sound = Gdx.audio.newSound(new BitFileHandle(a.data(82)));
            sounds.put(a.idStr(), sound);
            return sound;
        }
    }
    public static Sound getSound(String name, String path){
        if (sounds.containsKey(name))
            return sounds.get(name);
        else if (path != null) {
            Node node = NXFiles.Audio();
            Node n = node.subNode(path);
            Sound sound = Gdx.audio.newSound(new BitFileHandle(n.getAudio().data(82)));
            sounds.put(name, sound);
            return sound;
        }
        return null;
    }
    public static void play(String name, String path) {
        if (sounds.containsKey(name))
            sounds.get(name).play();
        else if (path != null) {
            Node node = NXFiles.Audio();
            Node n = node.subNode(path);
            Sound sound = Gdx.audio.newSound(new BitFileHandle(n.getAudio().data(82)));
            sounds.put(name, sound);
            sound.play();
        }
    }
    public static void play(String name){
        play(name, null);
    }
    public static void play(Audio audio){
        if(sounds.containsKey(audio.idStr())){
            sounds.get(audio.idStr()).play();
        } else {
            Sound sound = Gdx.audio.newSound(new BitFileHandle(audio.data(82)));
            sounds.put(audio.idStr(), sound);
            sound.play();
        }

    }
}
