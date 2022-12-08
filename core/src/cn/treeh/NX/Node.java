package cn.treeh.NX;

import java.io.IOException;

public class Node {
    //或许可以搞一个Cache

    public enum Type {
        none(0),
        integer(1),
        real(2),
        string(3),
        vector(4),
        bitmap(5),
        audio(6);
        int value;

        Type(int i) {
            value = i;
        }

        static Type getType(int v) {
            if (v > 6 || v < 0)
                v = 0;
            return values()[v];
        }
    }

    class Data {
        long pos;
        long name;
        long children;
        int num;
        Type type;
        long union;

        @Override
        public boolean equals(Object o) {
            Data node = (Data)o;
            return this.pos == node.pos && this.name == node.name && this.union == node.union;
        }
//       long ireal;
//       double dreal;
//       int string;
//       int[] vector = new int[2];
//       int bitmap_index;
//       int bitmap_wh;
//       int audio_index;
//       int audio_length;
    }

    Data m_data = null;
    NxFile f;

    @Override
    public boolean equals(Object o) {
        if(o instanceof Node) {
            Node node = (Node) o;
            return node.m_data.equals(this.m_data);
        }
        return false;
    }
    public int nChild(){
        return m_data.num;
    }
    public Node subNode(int index){
        if(index > m_data.num)
            return new Node(-1, f);
        return new Node(f.node_offset + m_data.children * 20L + index * 20L, f);
    }
    public Node(long m_data_pos, NxFile f) {
        try {
            this.f = f;
            if (m_data_pos == -1)
                return;
            f.seek(m_data_pos);
            this.m_data = new Data();
            m_data.pos = m_data_pos;
            m_data.name = f.readInt();
            m_data.children = f.readInt();
            long tmp = f.readInt();
            m_data.num = (int) tmp & 0xFF;
            m_data.type = Type.getType((int) tmp >> 16);
            m_data.union = f.readLong();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public Type getType(){
        return m_data != null ? m_data.type : Type.none;
    }
    private Node get_child(String s){
        String[] ss = s.split("[/]");
        Node res = this;
        for(String sss : ss)
            if(sss.trim().length() != 0) {
                res = res._get_child(sss);
            }
        return res;
    }
    private Node _get_child(String s) {
        //I can't guarantee this all file access won't have thread-safe problem
        //therefore add lock before to make it safe
        //I think it won't influence performance badly
        //Because except get_child need multi-seek call, other function only need seek once and read once.
        synchronized (f.fileReader) {
            if (m_data == null)
                return this;//保证程序不要出错
            long p = f.node_offset + m_data.children * 20L;
            long n = m_data.num;
            long p2, n2;
            int cmp, l2 = s.length();
            boolean z = false;
            long t = f.string_offset, sl;
            for (; ; ) {
                if (n == 0)
                    return new Node(-1, f);
                n2 = n >> 1;
                p2 = p + n2 * 20L;
                try {
                    f.seek(p2);
                    sl = t + f.readInt() * 8L;
                    f.seek(sl);
                    f.seek(f.readLong());
                    String name = f.fileReader.readUTF();
                    cmp = s.compareTo(name);
                    if (cmp < 0) {
                        n = n2;
                    } else if (cmp > 0) {
                        p = p2 + 20;
                        n -= n2 + 1;
                    } else {
                        return new Node(p2, f);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Node subNode(Object o) {
        return get_child(o.toString());
    }
    public long getInt(){
        return getInt(0);
    }
    public long getInt(long def){
        if (m_data == null)
            return def;
        switch (m_data.type) {
            case none:
            case vector:
            case bitmap:
            case audio:
                return def;
            case integer:
            case real:
                return m_data.union;
            case string:
                return Long.parseLong(getString("0"));
            default:
                System.err.println("Unknown node type");
        }
        return def;
    }
    public double getReal(){
        return getReal(0);
    }
    public double getReal(double def){
        if (m_data == null)
            return def;
        switch (m_data.type) {
            case none:
            case vector:
            case bitmap:
            case audio:
                return def;
            case integer:
            case real:
                return Double.longBitsToDouble(m_data.union);
            case string:
                return Double.parseDouble(getString("0"));
            default:
                System.err.println("Unknown node type");
        }
        return def;
    }
    public String getString(String def){
        if(m_data == null)
            return def;
        switch (m_data.type){
            case none:
            case vector:
            case bitmap:
            case audio:
                return def;
            case integer:
                return getInt() + "";
            case real:
                return getReal() + "";
            case string: {
                synchronized (f.fileReader) {
                    long sl = f.string_offset + 8 * m_data.union;
                    f.seek(sl);
                    try {
                        return f.fileReader.readUTF();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return def;
    }
    public String getName() {
        f.seek(f.string_offset + m_data.name * 8L);
        f.seek(f.readLong());
        try {
            return f.fileReader.readUTF();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public int[] getVector(){
        return getVector(new int[]{0, 0});
    }
    public int[] getVector(int[] def){
        if(m_data.type == Type.vector)
            return new int[]{(int) m_data.union, (int) (m_data.union >> 32)};
        return def;
    }
    public int x() {
        return m_data != null && m_data.type == Type.vector ? (int) m_data.union : 0;
    }

    public int y() {
        return m_data != null && m_data.type == Type.vector ? (int) (m_data.union >> 32) : 0;
    }

    public int size() {
        return m_data.num;
    }

    public  Audio getAudio() {
        if (m_data != null && m_data.type == Type.audio && f.audio_count > 0) {
            int audioLen = (int) (m_data.union >> 32);
            int audioIndex = (int) m_data.union;
            long pos = f.audio_offset + audioIndex * 8L;
            synchronized (f.fileReader) {
                f.seek(pos);
                pos = f.readLong();
            }
            return new Audio(pos, audioLen, f);
        }
        return new Audio(-1,  0,  f);
    }

    public Bitmap getBitmap() {
        if (m_data != null && m_data.type == Type.bitmap && f.bitmap_count > 0) {
            int bitmapIndex = (int) m_data.union;
            int width = (int) (m_data.union >> 32) & 0xFFFF;
            int height = (int) (m_data.union >> 48);
            long pos = f.bitmap_offset + bitmapIndex * 8L;
            synchronized (f.fileReader) {
                f.seek(pos);
                pos = f.readLong();
            }
            return new Bitmap(pos, width, height, f);
        }
        return new Bitmap(-1, 0, 0, f);
    }
    public Node getRoot(){
        return new Node(f.node_offset, f);
    }
    public boolean isRoot(){
        return m_data.pos == f.node_offset;
    }
}
