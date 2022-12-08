package cn.treeh.Audio;

import cn.treeh.Audio.util.BitFileHandle;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxFile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

public class BgmPlayer {
    static Music nowPlay = null;
    public static void play(String path){
        Node node = NXFiles.Audio();
        Node n = node.subNode(path);
        byte[] tmp = n.getAudio().data(82);
        if(nowPlay != null){
            nowPlay.stop();
            nowPlay.dispose();
        }
        nowPlay = Gdx.audio.newMusic(new BitFileHandle(tmp));
        nowPlay.setLooping(true);
        nowPlay.play();
    }
}
