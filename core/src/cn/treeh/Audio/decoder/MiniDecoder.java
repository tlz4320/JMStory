package cn.treeh.Audio.decoder;

import cn.treeh.Util.O;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MiniDecoder extends AbstractDecoder {

    private InputStream fileStream;

    /**
     * 鐢ㄦ寚瀹氱殑闊抽杈撳嚭audio鍒涘缓涓€涓狹iniPlayer銿
     * @param audio 闊抽杈撳嚭瀵硅薄銆傝嫢涿<b>null</b> 鍙В鐮佷笉浜х敓杈撳嚭銿
     */
    public MiniDecoder(IAudio audio) {
        super(audio);
    }

    /**
     * 鎵撳紑杈撳叆娴佸苟鍒濆鍖栬В鐮佸櫒銆?
     * @param input MP3byte銿
     * @return MP3甯уご绠€鐭俊鎭€?
     * @throws IOException 鍙戠敓I/O閿欒銆?
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
    boolean stop = false;
    boolean stopped = false;
    public void stop(){
        stop = true;
    }
    public void change(byte[] data){
        stop();
        while(!stopped);
        stopped = true;
        fileStream = new ByteArrayInputStream(data);
        stop = false;
        run();
    }

    @Override
    protected boolean cooperate() {
        return stop;
    }
    @Override
    public void reset(){
        try {
            fileStream.reset();
        }catch (Exception e){
            e.printStackTrace();
        }
    };
    @Override
    protected void done() {
        if (fileStream != null && !repeat) {
            try { fileStream.close(); } catch (IOException e) {e.printStackTrace(); }
        }
        stopped = true;
    }

}