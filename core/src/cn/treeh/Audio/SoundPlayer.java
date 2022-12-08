package cn.treeh.Audio;

import cn.treeh.Audio.util.BitFileHandle;
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
}
