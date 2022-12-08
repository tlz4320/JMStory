package cn.treeh.NX;

import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class NxFile {
    public long magic;
    public long node_count;//= read();
    public long node_offset;// = (((long) (read())) << 32) + read();
    public long string_count;// = read();
    public long string_offset;// = (((long) (read())) << 32) + read();
    public long bitmap_count;// = read();
    public long bitmap_offset;// = (((long) (read())) << 32) + read();
    public long audio_count;// = read();
    public long audio_offset;// = (((long) (read())) << 32) + read();

    public RandomAccessFile fileReader;
    public NxFile(String path){
        open(new File(path));
    }
    public  NxFile(File file){
        open(file);
    }
    void open(File name){
        try {
            fileReader = new RandomAccessFile(name, "r");
            //write with NoLifeNX file.cpp
            /*
                uint32_t const magic;
                uint32_t const node_count;
                uint64_t const node_offset;
                uint32_t const string_count;
                uint64_t const string_offset;
                uint32_t const bitmap_count;
                uint64_t const bitmap_offset;
                uint32_t const audio_count;
                uint64_t const audio_offset;

             */

            magic = readInt();
            node_count = readInt();
            node_offset = readLong();
            string_count = readInt();
            string_offset = readLong();
            bitmap_count = readInt();
            bitmap_offset = readLong();
            audio_count = readInt();
            audio_offset = readLong();
        }catch (Exception e){
            throw  new RuntimeException("File not found: + " + name);
        }
    }
    void skip(int s){
        try {
            fileReader.skipBytes(s);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    byte readByte(){
        try {
            return fileReader.readByte();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    int readUByte(){
        try {
            return fileReader.readUnsignedByte();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    long readInt(){
        int res = 0;
        res = readUByte() | (readUByte() << 8) |
                (readUByte() << 16) | (readUByte() << 24);
        return Integer.toUnsignedLong(res);
    }
    void seek(long pos){
        try{
            fileReader.seek(pos);
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    long readLong(){
        long res = readInt();
        res += readInt() << 32;
        return res;
    }
    Node root;
    public Node getNode(){
        if(root == null)
            root = new Node(node_offset, this);
        return root;
    }
}
