package cn.treeh.Audio;

import cn.treeh.Audio.util.BitFileHandle;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.NX.NxFile;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;

import java.util.Timer;
import java.util.TimerTask;

public class BgmPlayer {
    static Music nowPlay = null;
    static String nowPath = "";

    static float vol, prevol;
    public static void play(String path){
        if(nowPath.equals(path))
            return;
        nowPath = path;
        Node node = NXFiles.Audio();
        Node n = node.subNode(path);
        byte[] tmp = n.getAudio().data(82);
        if(nowPlay != null){
            vol = prevol = nowPlay.getVolume();
            System.out.println(prevol);
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    vol = vol - 0.1f;
                    nowPlay.setVolume(Math.max(0, vol));
                    if(vol < 0){
                        nowPlay.stop();
                        nowPlay.dispose();
                        nowPlay = Gdx.audio.newMusic(new BitFileHandle(tmp));
                        nowPlay.setVolume(prevol);
                        nowPlay.setLooping(true);
                        nowPlay.play();
                        this.cancel();
                    }
                }
            }, 0, 100);
        } else {
            nowPlay = Gdx.audio.newMusic(new BitFileHandle(tmp));
            nowPlay.setLooping(true);
            nowPlay.play();
        }

    }
}
