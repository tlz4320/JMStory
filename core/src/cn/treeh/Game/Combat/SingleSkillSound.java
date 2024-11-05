package cn.treeh.Game.Combat;

import cn.treeh.Audio.SoundPlayer;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.audio.Sound;

public class SingleSkillSound extends SkillSound{
    Sound usesound;
    Sound hitsound;
    public SingleSkillSound(String strid)
	{
        Node soundsrc = NXFiles.Audio().subNode("Skill.img").subNode(strid);

        usesound = SoundPlayer.getSound(soundsrc.subNode("Use"));
        hitsound = SoundPlayer.getSound(soundsrc.subNode("Hit"));
	}
    @Override
    public void play_use() {
        usesound.play();
    }

    @Override
    public void play_hit() {
        hitsound.play();
    }
}