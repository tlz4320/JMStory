package cn.treeh.NX;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.lz4.LZ4;
import org.lwjgl.util.lz4.LZ4StreamDecode;

import java.nio.ByteBuffer;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Bitmap {
    long pos;
    int width;
    public long getID(){
        return pos;
    }
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

    ByteBuffer decompress(byte[] input, int length){
        Inflater inflater = new Inflater();
        inflater.setInput(input,0, input.length);
        ByteBuffer output = BufferUtils.createByteBuffer(length);
        try {
            inflater.inflate(output);
            inflater.end();
            output.position(0);
            return output;
        } catch (Exception e) {
           return output;
        }
    }

    public int length(){
        return 4 * width * height;
    }
    public ByteBuffer data(){
        if(pos<0)
            return ByteBuffer.allocate(0);
        synchronized (f.file) {
            try {
                f.seek(pos);
                int size = (int)f.readInt();
                byte[] src = new byte[size];
                f.fileReader.readFully(src);
                return decompress(src, length());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}