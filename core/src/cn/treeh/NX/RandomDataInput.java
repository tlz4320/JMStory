package cn.treeh.NX;

import java.io.*;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

class RandomDataInput implements DataInput {
    RandomAccessFile randomAccessFile;
    MappedByteBuffer[] byteBuffer;
    int nowIndex = 0;
    MappedByteBuffer nowBuffer;
    long maxVal = Integer.toUnsignedLong(Integer.MAX_VALUE);
    public RandomDataInput(String path){
        this(new File(path));
    }
    public RandomDataInput(File file){

        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long fl = file.length();
            byteBuffer = new MappedByteBuffer[(int)(fl / (long)Integer.MAX_VALUE) + 1];
            int index = 0;
            while(fl > 0){
                byteBuffer[index] = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY,
                        Integer.toUnsignedLong(Integer.MAX_VALUE) * index, Math.min(Integer.MAX_VALUE, fl));
                index++;
                fl -= Integer.MAX_VALUE;
            }
            nowBuffer = byteBuffer[0];
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    public int read() throws IOException{
        return readUnsignedByte();
    }
    @Override
    public void readFully(byte[] b) throws IOException {
        readFully(b, 0, b.length);
    }

    @Override
    public void readFully(byte[] b, int off, int len) throws IOException {
        if(Integer.toUnsignedLong(len + nowBuffer.position()) > maxVal){
            if(nowBuffer.position() == maxVal) {
                nowBuffer = byteBuffer[++nowIndex];
                readFully(b, off, len);
                return;
            }
            int left = Integer.MAX_VALUE - nowBuffer.position();
            byteBuffer[nowIndex].get(b, off, left);
            nowBuffer = byteBuffer[++nowIndex];
            readFully(b, off + left, len - left);
        }
        else
            byteBuffer[nowIndex].get(b, off, len);
    }

    @Override
    public int skipBytes(int n) throws IOException {
        if(Integer.toUnsignedLong(n + nowBuffer.position()) > maxVal){
            nowBuffer = byteBuffer[++nowIndex];
            skipBytes(n - (Integer.MAX_VALUE - nowBuffer.position()));
        }
        nowBuffer.position(nowBuffer.position() + n);
        return n;
    }

    @Override
    public boolean readBoolean() throws IOException {
        int ch = Byte.toUnsignedInt(readByte());
        return (ch != 0);
    }

    @Override
    public byte readByte() throws IOException {
        if(byteBuffer[nowIndex].position() == Integer.MAX_VALUE){
            nowBuffer = byteBuffer[++nowIndex];
        }
        return nowBuffer.get();
    }

    @Override
    public int readUnsignedByte() throws IOException {
        return Byte.toUnsignedInt(readByte());
    }

    @Override
    public short readShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        return (short)((ch1 << 8) + (ch2));

    }

    @Override
    public int readUnsignedShort() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        return (ch1 << 8) + (ch2);
    }

    @Override
    public char readChar() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        return (char)((ch1 << 8) + (ch2));
    }

    @Override
    public int readInt() throws IOException {
        int ch1 = this.read();
        int ch2 = this.read();
        int ch3 = this.read();
        int ch4 = this.read();
        return ((ch1 << 24) + (ch2 << 16) + (ch3 << 8) + (ch4));
    }

    @Override
    public long readLong() throws IOException {
        return ((long)(readInt()) << 32) + (readInt() & 0xFFFFFFFFL);
    }

    @Override
    public float readFloat() throws IOException {
        return Float.intBitsToFloat(readInt());
    }

    @Override
    public double readDouble() throws IOException {
        return Double.longBitsToDouble(readLong());
    }

    @Override
    public String readLine() throws IOException {
        StringBuilder input = new StringBuilder();
        int c = -1;
        boolean eol = false;

        while (!eol) {
            switch (c = read()) {
                case -1:
                case '\n':
                    eol = true;
                    break;
                case '\r':
                    eol = true;
                    int curIndex = nowIndex;
                    int cur = nowBuffer.position();
                    if ((read()) != '\n') {
                        nowIndex = curIndex;
                        nowBuffer = byteBuffer[curIndex];
                        nowBuffer.position(cur);
                    }
                    break;
                default:
                    input.append((char)c);
                    break;
            }
        }

        if ((c == -1) && (input.length() == 0)) {
            return null;
        }
        return input.toString();
    }

    @Override
    public String readUTF() throws IOException {
        return DataInputStream.readUTF(this);
    }
    public void seek(long p){
        nowIndex = (int)(p / maxVal);
        nowBuffer = byteBuffer[nowIndex];
        nowBuffer.position((int)(p % maxVal));
    }
}