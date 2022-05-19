package cn.treeh.NX;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.lz4.LZ4;
import org.lwjgl.util.lz4.LZ4StreamDecode;

import java.nio.ByteBuffer;

public class Bitmap {
    long pos;
    int width, height;
    File f;
    public Bitmap(long pos, int width, int height, File f){
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.f = f;
    }
    public int length(){
        return 4 * width * height;
    }
    public byte[] data(){
        if(pos<0)
            return new byte[0];
        synchronized (f.fileReader) {
            try {
                f.seek(pos);
                int size = (int)f.readInt();
                byte[] src = new byte[length()];
                f.fileReader.readFully(src);
                ByteBuffer bf = BufferUtils.createByteBuffer(size);
                bf.put(src, 0, size);
                bf.position(0);
                ByteBuffer des = BufferUtils.createByteBuffer(length());
                LZ4.LZ4_decompress_safe(bf, des);
                des.get(src, 0, src.length);
                return src;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}