package cn.treeh.NX;

import com.badlogic.gdx.Gdx;

import java.io.File;
import java.io.RandomAccessFile;

public class FixBug {
    public static void main(String[] args) throws Exception{
        RandomAccessFile randomAccessFile = new RandomAccessFile("Map.nx", "rw");
        Node node = new NxFile(new File("Map.nx")).getNode().subNode("Obj/connect.img/rope/0/0/0/origin");
        randomAccessFile.seek(node.m_data.pos);
        long data = 32;
        data = data << 32 + 1;
        randomAccessFile.writeLong(data);
        randomAccessFile.close();
    }
}
