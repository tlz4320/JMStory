package cn.treeh.Audio.util;

import com.badlogic.gdx.files.FileHandle;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class BitFileHandle extends FileHandle {
    byte[] data;
    public BitFileHandle(byte[] data){
        this.data = data;
    }
    @Override
    public String extension() {
        return "mp3";
    }

    @Override
    public InputStream read() {
        return new ByteArrayInputStream(data);
    }
}
