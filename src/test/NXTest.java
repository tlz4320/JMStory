import cn.treeh.NX.File;
import cn.treeh.NX.Node;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.ALC10.*;

public class NXTest {
    public static void main(String[] args) {
        File file = new File("D:\\1\\mxd\\冒险岛online\\Etc.nx");
        Node node = file.getNode();
        Node n = node.subNode("SpeedAnimationQuiz.img");
        n = n.subNode("BeijingOlympic");
        n = n.subNode("AniQuiz");
        n = n.subNode("1");
        n = n.subNode("ani");
        n = n.subNode("0");
        byte[] tmp = n.getBitmap().data();
        try {
            ByteBuffer bf = BufferUtils.createByteBuffer(tmp.length);
            bf.put(tmp);
            bf.position(0);
            long device = alcOpenDevice((ByteBuffer)null);
            ALCCapabilities deviceCaps = ALC.createCapabilities(device);

            long context = alcCreateContext(device, (IntBuffer) null);
            alcMakeContextCurrent(context);
            AL.createCapabilities(deviceCaps);
            int buffer = alGenBuffers();
            alBufferData(buffer, AL_FORMAT_STEREO16, bf, 705000);
            int source = alGenBuffers();
            alSourcef(source, AL_BUFFER, buffer);
            alSourcei(source, AL_LOOPING, AL_TRUE);
            alSourcePlay(source);


            while (true){}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}