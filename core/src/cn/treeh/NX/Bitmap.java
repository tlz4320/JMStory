package cn.treeh.NX;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.lz4.LZ4;
import org.lwjgl.util.lz4.LZ4StreamDecode;

import java.nio.ByteBuffer;

public class Bitmap {
    long pos;
    int width;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    int height;
    NxFile f;
    public Bitmap(long pos, int width, int height, NxFile f){
        this.pos = pos;
        this.width = width;
        this.height = height;
        this.f = f;
    }
    public int length(){
        return 4 * width * height;
    }
    public ByteBuffer data(){
        if(pos<0)
            return ByteBuffer.allocate(0);
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
                return des;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}