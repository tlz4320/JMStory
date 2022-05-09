import cn.treeh.Audio.decoder.Header;
import cn.treeh.Audio.decoder.MAudio;
import cn.treeh.Audio.decoder.MiniDecoder;
import cn.treeh.NX.File;
import cn.treeh.NX.Node;
import org.lwjgl.BufferUtils;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.ALC;
import org.lwjgl.openal.ALCCapabilities;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.SourceDataLine;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.LinkedList;

import static org.lwjgl.openal.AL10.*;
import static org.lwjgl.openal.AL10.alSourcePlay;
import static org.lwjgl.openal.ALC10.*;

public class AudioTest {
    public static void main(String[] args) {
        File file = new File("D:\\program\\project\\JourneyClient\\src\\Sound.nx");
        Node node = file.getNode();
        Node n = node.subNode("BgmUI.img").subNode("Title");
        byte[] tmp = n.getAudio().data(82);
        MiniDecoder player = new MiniDecoder(new MAudio());
        player.open(tmp);
        player.run();
        LinkedList<byte[]> res = ((MAudio)player.getAudio()).dataline;
        int size = 0;
        for(byte[] b : res)
            size += b.length;
        byte[] lr = new byte[size];
        int pos = 0;
        for(byte[] b: res){
            System.arraycopy(b, 0, lr, pos, b.length);
            pos += b.length;
        }
        Header h = player.getHeader();
        AudioFormat af = new AudioFormat(h.getSamplingRate(), 16, h.getChannels(), true, false);
        try {
            SourceDataLine dataLine = AudioSystem.getSourceDataLine(af);

            dataLine.open(af, lr.length);
            int ttt = dataLine.write(lr, 0, lr.length);
            dataLine.start();
        }catch (Exception e){

        }
//        ByteBuffer bf = BufferUtils.createByteBuffer(lr.length);
//        bf.put(lr);
//        long device = alcOpenDevice((ByteBuffer)null);
//        ALCCapabilities deviceCaps = ALC.createCapabilities(device);
//        long context = alcCreateContext(device, (IntBuffer) null);
//        alcMakeContextCurrent(context);
//        AL.createCapabilities(deviceCaps);
//        int buffer = alGenBuffers();
//        alBufferData(buffer, AL_FORMAT_STEREO8, bf, 22050);
//        int source = alGenBuffers();
//        alSourcef(source, AL_BUFFER, buffer);
//        alSourcePlay(source);
        while(true){}
    }
}
