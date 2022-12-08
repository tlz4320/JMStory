package cn.treeh.Audio.decoder;

import cn.treeh.Util.O;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MiniDecoder extends AbstractDecoder {

    private InputStream fileStream;


    /**
     * 用指定的音频输出audio创建一个MiniPlayer。
     * @param audio 音频输出对象。若为 <b>null</b> 只解码不产生输出。
     */
    public MiniDecoder(IAudio audio) {
        super(audio);
    }

    /**
     * 打开输入流并初始化解码器。
     * @param input MP3 byte数据。
     * @return MP3帧头简短信息。
     * @throws IOException 发生I/O错误。
     */
    public String open(byte[] input) {
        fileStream = new ByteArrayInputStream(input);
        return super.openDecoder();
    }

    @Override
    protected int fillBuffer(byte[] b, int off, int len) {
        try {
            return fileStream.read(b, off, len);
        } catch (Exception e) {
            return -1;
        }
    }

//    public void change(byte[] data){
//        pause();
//        fileStream = new ByteArrayInputStream(data);
//        openDecoder();
//        stop = true;
//        pause();
//    }
//    @Override
//    public void reset(){
//        try {
//            fileStream.reset();
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//    };
    @Override
    protected void done() {
        if (fileStream != null) {
            try { fileStream.close(); } catch (IOException e) {e.printStackTrace(); }
        }
    }

}