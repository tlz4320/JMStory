package cn.treeh.NX;

import cn.treeh.Util.O;
import org.lwjgl.BufferUtils;
import org.lwjgl.util.lz4.LZ4;

import java.io.EOFException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.function.Predicate;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Convert{
    static int[] key_bms = new int[65536];
    static int[] key_kms = new int[65536];
    static int[] key_gms = new int[65536];
    static void genKey(){
        try{
            RandomAccessFile fi = new RandomAccessFile("key.bt", "r");
            for(int index = 0; index < 65536; index++){
                key_bms[index] = fi.readUnsignedByte();
            }
            for(int index = 0; index < 65536; index++){
                key_kms[index] = fi.readUnsignedByte();
            }
            for(int index = 0; index < 65536; index++){
                key_gms[index] = fi.readUnsignedByte();
            }
        }catch (Exception e){

        }
    }
    static{
        genKey();
    }
    int[] table4 = {0x00, 0x11, 0x22, 0x33, 0x44, 0x55, 0x66, 0x77,
            0x88, 0x99, 0xAA, 0xBB, 0xCC, 0xDD, 0xEE, 0xFF};
    int[] table5 = {0x00, 0x08, 0x10, 0x19, 0x21, 0x29, 0x31, 0x3A, 0x42, 0x4A, 0x52,
            0x5A, 0x63, 0x6B, 0x73, 0x7B, 0x84, 0x8C, 0x94, 0x9C, 0xA5, 0xAD,
            0xB5, 0xBD, 0xC5, 0xCE, 0xD6, 0xDE, 0xE6, 0xEF, 0xF7, 0xFF};
    // TODO - Use AES to generate these keys at runtime
    int[][] keys = {key_bms, key_gms, key_kms};
    // Tables for color lookups
    enum type {
        none(0),
        integer(1),
        real(2),
        string(3),
        vector(4),
        bitmap(5),
        audio(6),
        uol(7);
        int value;

        type(int i) {
            value = i;
        }

        static type getType(int v) {
            if (v > 6 || v < 0)
                v = 0;
            return values()[v];
        }
    }
    class node{
        long name = 0;
        long children = 0;
        int num = 0;
        type type = Convert.type.none;
        long data;
    }
    class audio {
        long length;
        long data;
    }
    class bitmap{
        long data;
        int[] key;
    }
    HashMap<Integer, node> nodes;
    HashMap<Integer, Integer> imgs;
    LinkedList<Map.Entry<Integer, Integer>> nodes_to_sort;
    int[] u8Key, u16Key;
    static void E(String s){
        throw new RuntimeException(s);
    }
    class InFile{
        public RandomAccessFile fileReader;
        void skipBytes(int s){
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
        int readUnsignedByte(){
            try {
                return fileReader.readUnsignedByte();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        int readInt(){
            return (int)readUInt();
        }
        char readChar(){
            try {
                int ch1 = fileReader.read();
                int ch2 = fileReader.read();
                if ((ch1 | ch2) < 0) {
                    throw new EOFException();
                } else {
                    return (char)((ch2 << 8) + ch1);
                }
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        long getFilePointer(){
            try {
                return fileReader.getFilePointer();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        long readUInt(){
            int res = 0;
            res = readUnsignedByte() | (readUnsignedByte() << 8) |
                    (readUnsignedByte() << 16) | (readUnsignedByte() << 24);
            return Integer.toUnsignedLong(res);
        }
        void readFully(byte[] b){
            try {
                fileReader.readFully(b);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        void seek(long pos){
            try{
                fileReader.seek(pos);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        long readLong(){
            long res = readUInt();
            res += readUInt() << 32;
            return res;
        }
    }

    class OutFile{
        public RandomAccessFile fileWriter;
        void writeInt(int v){
            try {
                fileWriter.write(v & 255);
                fileWriter.write((v >>> 8) & 255);
                fileWriter.write((v >>> 16) & 255);
                fileWriter.write((v >>> 24) & 255);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        void writeLong(long v){
            try {
                writeInt((int)v);
                writeInt((int)(v >>> 32));
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        void writeUTF(String s){
            try {
                fileWriter.writeUTF(s);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        void write(byte b){
            try {
                fileWriter.write(b);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        void write(byte[] b, int len){
            try {
                fileWriter.write(b, 0, len);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        void write(byte[] b){
            try {
                fileWriter.write(b);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        long getFilePointer(){
            try {
                return fileWriter.getFilePointer();
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }

        void seek(long pos){
            try{
                fileWriter.seek(pos);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }

    }

    public InFile fileReader;
    public OutFile fileWriter;
    String filename;
    HashMap<String, Integer> strings;
    TreeMap<Integer, String> rStrings;
    LinkedList<bitmap> bitmaps;
    LinkedList<audio> audios;
    LinkedList<Integer> uol_path;
    LinkedList<LinkedList<Integer>> uols;
    LinkedList<Integer> link_path;
    LinkedList<LinkedList<Integer>> links;
    long fileStart;
    public int readCInt() throws Exception{
        byte tmp = fileReader.readByte();
        return tmp != -128 ? tmp : fileReader.readInt();
    }
    public void convert(String filename){
        link_path = new LinkedList<>();
        links = new LinkedList<>();
        uols = new LinkedList<>();
        uol_path = new LinkedList<>();
        audios = new LinkedList<>();
        bitmaps = new LinkedList<>();
        strings = new HashMap<>();
        rStrings = new TreeMap<>();
        nodes  = new HashMap<>();
        nodes.put(0, new node());
        imgs = new HashMap<>();
        nodes_to_sort = new LinkedList<>();
        this.filename = filename;
        if(!filename.toLowerCase(Locale.ROOT).endsWith("wz")) {
            O.ptln("Not a Wz file");
        }
        try {
            fileReader = new InFile();
            fileReader.fileReader = new RandomAccessFile(filename, "r");
            fileWriter = new OutFile();
            fileWriter.fileWriter = new RandomAccessFile(filename.replace(".wz", ".nx"), "rw");
            fileWriter.seek(0);
            parse_file();
            outputFile();
            write_nodes();
            write_audio();
            write_bitmaps();
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }
    void makeU16Key(){
        u16Key = new int[(int)Math.ceil(u8Key.length / 2d)];
        for(int index = 0; index < u8Key.length; index += 2){
            u16Key[index / 2] =
                    u8Key[index] + (u8Key[index + 1] << 8);
        }
    }
    void deduceKey() throws Exception{
        int len = fileReader.readByte();
        if(len >= 0)
            E("Something Wrong " + filename);
        long slen = len == -128 ? fileReader.readUInt() : -len;
        long nowPos = fileReader.getFilePointer();
        for(int[] key : keys){
            fileReader.seek(nowPos);
            int mask = 0xAA;
            boolean valid = true;
            for(long i = 0; i < slen && i < key.length; i++, mask++){
                int c = fileReader.readUnsignedByte() ^ key[(int)i] ^ mask;
                if(c < 0x20 || c >= 0x80) {
                    valid = false;
                    break;
                }
            }
            if(valid){
                u8Key = key;
            }
        }
        if(u8Key == null)
            E("Can't identity the locale");
        makeU16Key();
        fileReader.seek(nowPos + slen);
    }
    int addString(String s){
        if(strings.containsKey(s))
            return strings.get(s);
        strings.put(s, strings.size());
        rStrings.put(strings.size() - 1, s);
        return strings.size() - 1;
    }
    long read_enc_string() throws Exception{
        int len = fileReader.readByte();
        if(len > 0){
            long slen = len == 127 ? fileReader.readUInt() : len;
            long mask = 0xAAAA;
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < Math.min(slen, u16Key.length); i++){
                stringBuilder.append((char)(fileReader.readChar() ^ u16Key[i] ^ mask));
                mask++;
            }
            for(int i = u16Key.length; i < slen; i++){
                stringBuilder.append((char)(fileReader.readChar() ^ mask));
                mask++;
            }
            return addString(stringBuilder.toString());
        }
        if(len < 0){
            long slen = len == -128 ? fileReader.readUInt() : -len;
            int mask = 0xAA;
            StringBuilder stringBuilder = new StringBuilder();
            for(int i = 0; i < Math.min(slen, u8Key.length); i++){
                stringBuilder.append((char)(fileReader.readUnsignedByte() ^ u8Key[i] ^ mask));
                mask++;
            }
            for(int i = u8Key.length; i < slen; i++){
                stringBuilder.append((char)(fileReader.readUnsignedByte() ^ mask));
                mask++;
            }
            //strange? why have to back insert in c++ version
            return addString(stringBuilder.toString());
        }
        return 0;
    }
    void directory(int id) throws Exception {
        LinkedList<Integer> directories = new LinkedList<>();
        node n = nodes.get(id);
        int count = readCInt();
        int nodeSize = nodes.size();
        n.num = count;
        n.children = nodeSize;
        for(int index = 0; index < count; index++){
            nodes.put(index + nodeSize, new node());
        }
        for (int i = 0; i < count; i++) {
            node nn = nodes.get(i + nodeSize);
            int type = fileReader.readUnsignedByte();
            switch (type) {
                case 1:
                    E("Unknown node type 1");
                    break;
                case 2: {
                    int s = fileReader.readInt();
                    long p = fileReader.getFilePointer();
                    fileReader.seek(fileStart + 2);
                    type = fileReader.readUnsignedByte();
                    nn.name = read_enc_string();
                    fileReader.seek(p);
                    break;
                }
                case 3:
                case 4:
                    nn.name = read_enc_string();
                    break;
                default: {
                    E("Unknown directory type");
                }
            }
            int size = readCInt();
            if (size < 0)
                E("Directory/Img has error size!");
            readCInt();
            fileReader.skipBytes(4);
            if (type == 3)
                directories.add(nodeSize + i);
            else if (type == 4)
                imgs.put(nodeSize + i, size);
            else
                E("Unknown type 2 directory");
        }
        for (int it : directories)
            directory(it);
        nodes_to_sort.add(new AbstractMap.SimpleEntry<>(nodeSize, count));
    }
    long read_prop_string(long pos) throws Exception{
        int a = fileReader.readUnsignedByte();
        switch (a){
            case 0x00:
            case 0x73: return read_enc_string();
            case 0x01:
            case 0x1B:
            {
                long o = fileReader.readInt() + pos;
                long nowPos = fileReader.getFilePointer();
                fileReader.seek(o);
                long s = read_enc_string();
                fileReader.seek(nowPos);
                return s;
            }
            default:
                E("Unknown property string type " + a);
        }
        return 0;
    }
    void sub_property(int node, long pos) throws  Exception{
        node n = nodes.get(node);
        int count = readCInt();
        int ni = nodes.size();
        n.num = count;
        n.children = ni;
        for(int i = 0; i < count; i++){
            nodes.put(ni + i, new node());
        }
        for(int i = 0; i < count; i++){
            node nn = nodes.get(i + ni);
            nn.name = read_prop_string(pos);
            int type = fileReader.readUnsignedByte();
            int num;
            long p;
            switch (type){
                case 0x00:
                    nn.type = Convert.type.integer;
                    nn.data = i;
                    break;
                case 0x0B:
                case 0x02:
                    nn.type = Convert.type.integer;
                    nn.data = Integer.toUnsignedLong(fileReader.readChar());
                    break;
                case 0x03:
                case 0x13:
                    nn.type = Convert.type.integer;
                    nn.data = readCInt();
                    break;
                case 0x04:
                    nn.type = Convert.type.real;
                    num = fileReader.readUnsignedByte();
                    if(num == 0x80){
                        //this is float
                        //but java hard to process float in bit process
                        //therefore read as int and convert in game
                        nn.data = fileReader.readInt();
                    }else{
                        nn.data = num;
                    }
                    break;
                case 0x05:
                    nn.type = Convert.type.real;
                    nn.data = fileReader.readLong();
                    break;
                case 0x08:
                    nn.type = Convert.type.string;
                    nn.data = read_prop_string(pos);
                    break;
                case 0x09:
                    p = fileReader.readInt() + fileReader.getFilePointer();
                    extended_property(ni + i, pos);
                    fileReader.seek(p);
                    break;
                case 0x14:
                    nn.type = Convert.type.integer;
                    num = fileReader.readUnsignedByte();
                    if(num == 0x80){
                        nn.data = fileReader.readLong();
                    }
                    else{
                        nn.data = fileReader.readByte();
                    }
                    break;
                default:
                    E("Unknown sub property type: " + type);
            }
        }
        nodes_to_sort.add(new AbstractMap.SimpleEntry<>(ni, count));
    }
    void extended_property(int node, long pos) throws Exception{
        node n = nodes.get(node);
        String st = rStrings.get((int)read_prop_string(pos));

        if(st.equals("Property")){
            fileReader.skipBytes(2);
            sub_property(node, pos);
        } else if(st.equals("Canvas")){
            fileReader.skipBytes(1);
            if(fileReader.readUnsignedByte() == 1){
                fileReader.skipBytes(2);
                sub_property(node, pos);
            }
            n.type = type.bitmap;
            n.data = bitmaps.size();
            bitmap bm = new bitmap();
            bm.data = fileReader.getFilePointer();
            bm.key = u8Key;
            bitmaps.add(bm);
            n.data += (Integer.toUnsignedLong(readCInt())) << 32;
            n.data += (Integer.toUnsignedLong(readCInt())) << 48;
        } else if(st.equals("Shape2D#Vector2D")){
            n.type = type.vector;
            n.data = readCInt();
            n.data = (Integer.toUnsignedLong(readCInt())) << 32;
        } else if(st.equals("Shape2D#Convex2D")){
            int count = readCInt();
            int ni = nodes.size();
            n.num = count;
            n.children = ni;
            for(int index = 0; index < count; index++){
                nodes.put(ni + index, new node());
            }
            for(int index = 0; index < count; index++){
                node nn = nodes.get(ni + index);
                nn.name = addString(index + "");
                extended_property(ni, pos);
            }
            nodes_to_sort.add(new AbstractMap.SimpleEntry<>(ni, count));
        } else if(st.equals("Sound_DX8")){
            n.type = type.audio;
            n.data = audios.size();
            audio a = new audio();
            fileReader.skipBytes(1);
            a.length = readCInt() + 82;
            n.data += (a.length) << 32;
            readCInt();
            a.data = fileReader.getFilePointer();
            audios.add(a);
        } else if(st.equals("UOL")){
            fileReader.skipBytes(1);
            n.type = type.uol;
            n.data = read_prop_string(pos);
        } else
            E("Unknown sub property type: " + st);
    }
    void lua_script(int node) throws Exception{
        long slen = readCInt();
        if(slen > 0x1ffff)
            E("Lua script is too long");
        StringBuilder stringBuilder = new StringBuilder();
        for(int index = 0; index < slen; index++){
            stringBuilder.append(fileReader.readByte() ^ u8Key[index]);
        }
        int st = addString(stringBuilder.toString());
        node n = nodes.get(node);
        n.type = type.string;
        n.data = st;
    }
    void img(int node, int size) throws Exception{
        long p = fileReader.getFilePointer();
        int n1 = fileReader.readUnsignedByte();
        if(n1 == 1){
            lua_script(node);
        }
        else{
            deduceKey();
            fileReader.seek(p);
            extended_property(node, p);
        }
        fileReader.seek(p + size);
    }

    void parse_file() throws Exception{
        O.ptln("Parsing file");
        long magic = fileReader.readInt();
        if(magic != 0x31474B50)
            O.ptln("Not a WZ file  " + filename);
        fileReader.skipBytes(8);
        fileStart = fileReader.readInt();
        fileReader.seek(fileStart + 2);
        readCInt();
        fileReader.skipBytes(1);
        deduceKey();
        fileReader.seek(fileStart + 2);
        addString("");
        directory(0);
        for(Map.Entry<Integer, Integer> it: imgs.entrySet())
            img(it.getKey(), it.getValue());
        O.ptln("Parsing file OK!");
        parse_uol();
    }
    node[] orderedNode;
    void sortNode(){
        orderedNode = new node[nodes.size()];
        for(Map.Entry<Integer, node> node : nodes.entrySet()){
            orderedNode[node.getKey()] = node.getValue();
        }
        for(Map.Entry<Integer, Integer> nts : nodes_to_sort){
            Arrays.parallelSort(orderedNode, nts.getKey(), nts.getValue() + nts.getKey(),
                    new Comparator<node>() {
                        @Override
                        public int compare(node n1, node n2) {
                            return rStrings.get((int)n1.name).compareTo(rStrings.get((int)n2.name));
                        }
                    });
        }
    }
    void find_uols(int node){
        node n = orderedNode[node];
        if(n.type == type.uol){
            uol_path.push(node);
            uols.add((LinkedList<Integer>) uol_path.clone());
            uol_path.pop();
        }
        else if (n.num != 0){
            uol_path.push(node);
            for(int i = 0; i < n.num; i++)
                find_uols((int)n.children + i);
            uol_path.pop();
        }
    }
    int getChild(int pnode, String s){
        if(pnode == 0)
            return 0;
        node n = orderedNode[pnode];
        int i;
        for(i = 0; i < n.num; i++){
            if(rStrings.get((int)orderedNode[(int) (i + n.children)].name).compareTo(s) == 0)
                return (int) (i + n.children);
        }
        return 0;
    }
    boolean resolve_uol(LinkedList<Integer> uol) {
        uol = (LinkedList<Integer>)uol.clone();
        node n = orderedNode[uol.pop()];
        if (n.type != type.uol)
            E("None UOL get into UOL");
        String s = rStrings.get((int) n.data);
        int b = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '/') {
                if (i - b == 2 &&
                        s.charAt(b) == '.' &&
                        s.charAt(b + 1) == '.')
                    uol.pop();
                else
                    uol.push(getChild(uol.getFirst(), s.substring(b, i)));
                b = ++i;
            }
        }

        uol.push(getChild(uol.getFirst(), s.substring(b)));
        if (uol.getFirst() == 0)
            return false;
        node nr = orderedNode[uol.getFirst()];
        if (nr.type == type.uol)
            return false;
        n.type = nr.type;
        n.children = nr.children;
        n.num = nr.num;
        n.data = nr.data;
        return true;

    }
    void uol_fail(LinkedList<Integer> uol){
        node n = orderedNode[uol.getFirst()];
        if(n.type == type.uol){
            n.type = type.none;
        }
        else{
            O.ptEln("Not uol in uol");
        }
    }
    void find_links(int link_node, String str){
        node n = orderedNode[link_node];
        String s = rStrings.get((int)n.name);
        if(s.equals(str)){
            link_path.push(link_node);
            links.add((LinkedList<Integer>) link_path.clone());
            link_path.pop();
        }
        else if(n.num != 0){
            link_path.push(link_node);
            for(int i = 0; i < n.num; i++){
                find_links((int)n.children + i, str);
            }
            link_path.pop();
        }
    }
    int get_child_full(int pnode, String str){
        node n = orderedNode[pnode];
        int i;
        for(i = 0; i < n.num; i++){
            if(rStrings.get((int)orderedNode[(int) (i + n.children)].name).compareTo(str) == 0)
                return (int)(i + n.children);
        }
        return 0;
    }
    boolean resolve_source(LinkedList<Integer> link){
        link = (LinkedList<Integer>)link.clone();
        node n = orderedNode[link.pop()];
        String s = rStrings.get((int)n.data);
        String[] parts = s.split("[/]");
        int r = 0;
        for(String part : parts){
            r = get_child_full(r, part);
        }
        if(r == 0)
            return false;
        node nr = orderedNode[r];
        node pn = orderedNode[link.getFirst()];
        pn.data = nr.data;
        return true;
    }
    void source_fail(LinkedList<Integer> link, String str){
        node n = orderedNode[link.getFirst()];
        O.ptEln("Failed to find " + str + " for [" + rStrings.get(n.data) + "].");
    }
    boolean resolve_outlink(LinkedList<Integer> link){
        link = (LinkedList<Integer>)link.clone();
        node n = orderedNode[link.pop()];
        String s = rStrings.get((int)n.data);
        String[] parts = s.split("[/]");
        if(parts[0].equals("Map"))
            return true;
        int r = 0;
        for(String part : parts)
            r = get_child_full(r, part);
        if(r == 0)
            return false;
        node nr = orderedNode[r];
        node pn = orderedNode[link.getFirst()];
        pn.data = nr.data;
        return true;
    }
    boolean resolve_inlink(LinkedList<Integer> link){
        link = (LinkedList<Integer>)link.clone();
        node n = orderedNode[link.pop()];
        String s = rStrings.get((int)n.data);
        String[] parts = s.split("[/]");
        int r = link.getFirst();
        node pn = orderedNode[r];
        for(;;){
            for(String part : parts){
                r = get_child_full(r, part);
                if(r == 0)
                    break;
            }
            if(r != 0)
                break;
            link.pop();
            if(link.size() == 0)
                break;
            r = link.getFirst();
        }
        if(r == 0)
            return false;
        node nr = orderedNode[r];
        pn.data = nr.data;
        return true;
    }
    void parse_uol(){
        sortNode();
        O.ptln("Parsing uol");
        find_uols(0);
        for(;;){
            boolean diff = uols.removeIf(new Predicate<LinkedList<Integer>>() {
                @Override
                public boolean test(LinkedList<Integer> v) {
                    return resolve_uol(v);
                }
            });
            if(!diff)
                break;
        }
        for(LinkedList<Integer> it : uols)
            uol_fail(it);
        O.ptln("Done!");
        O.ptln("Parsing source");
        find_links(0, "source");
        for(;;){
            boolean diff = links.removeIf(new Predicate<LinkedList<Integer>>() {
                @Override
                public boolean test(LinkedList<Integer> v) {
                    return resolve_source(v);
                }
            });
            if(!diff)
                break;
        }
        for(LinkedList<Integer> it : links)
            source_fail(it, "source");
        links.clear();
        O.ptln("Done");
        O.ptln("Parsing _outlink");
        find_links(0, "_outlink");
        for(;;){
            boolean diff = links.removeIf(new Predicate<LinkedList<Integer>>() {
                @Override
                public boolean test(LinkedList<Integer> v) {
                    return resolve_outlink(v);
                }
            });
            if(!diff)
                break;
        }
        for(LinkedList<Integer> it : links)
            source_fail(it, "_outlink");
        links.clear();
        O.ptln("Done");
        O.ptln("Parsing _inlink");
        find_links(0, "_inlink");
        for(;;){
            boolean diff = links.removeIf(new Predicate<LinkedList<Integer>>() {
                @Override
                public boolean test(LinkedList<Integer> v) {
                    return resolve_inlink(v);
                }
            });
            if(!diff)
                break;
        }
        for(LinkedList<Integer> it : links)
            source_fail(it, "_inlink");
        links.clear();
        O.ptln("Done");
    }
    long offset, node_offset, string_offset, string_table_offset, bitmap_offset,
            bitmap_table_offset, audio_offset, audio_table_offset;
    void calculate_offsets() throws Exception {
        offset = 0;
        offset += 52;
        offset += 0x10 - (offset & 0xf);
        node_offset = offset;
        fileWriter.writeLong(node_offset);

        //24 for add length
        offset += nodes.size() * 20L;
        offset += 0x10 - (offset & 0xf);
        string_table_offset = offset;
        fileWriter.writeInt(strings.size());
        fileWriter.writeLong(string_table_offset);
        offset += strings.size() * 8L;
        offset += 0x10 - (offset & 0xf);
        string_offset = offset;
        //the reason I write string here is for java UTF support
        //In this way I could get real offset for Unicode
        long nowPos = fileWriter.getFilePointer();
        write_strings();
        fileWriter.seek(nowPos);
        offset += 0x10 - (offset & 0xf);
        audio_table_offset = offset;

        offset += audios.size() * 8L;
        offset += 0x10 - (offset & 0xf);

        bitmap_table_offset = offset;
        offset += bitmaps.size() * 8L;
        offset += 0x10 - (offset & 0xf);
        audio_offset = offset;
        for (audio a : audios) {
            offset += a.length;
        }
        bitmap_offset = offset;

    }
    void outputFile() throws Exception{
        O.ptln("Begin to output");
        fileWriter.writeInt(0x34474B50);
        fileWriter.writeInt(nodes.size());
        calculate_offsets();
        fileWriter.writeInt(bitmaps.size());
        fileWriter.writeLong(bitmap_table_offset);
        fileWriter.writeInt(audios.size());
        fileWriter.writeLong(audio_table_offset);
        O.ptln("Header Done!");
    }
    void write_nodes() throws Exception{
        fileWriter.seek(node_offset);
        for(node n : orderedNode) {
            fileWriter.writeInt((int)n.name);
            fileWriter.writeInt((int)n.children);
            fileWriter.writeInt((n.type.value << 16) | n.num);
            fileWriter.writeLong(n.data);
        }
    }
    void write_strings() throws Exception{
        fileWriter.seek(string_offset);
        long[] strPos = new long[strings.size() + 1];
        strPos[0] = string_offset;
        int index = 0;
        for(String s : rStrings.values()){
            fileWriter.writeUTF(s);
            //get real write pos;
            strPos[++index] = fileWriter.getFilePointer();
        }
        offset = fileWriter.getFilePointer();
        fileWriter.seek(string_table_offset);
        for(index = 0; index < strPos.length - 1; index++){
            fileWriter.writeLong(strPos[index]);
        }

    }
    void write_audio() throws Exception{
        fileWriter.seek(audio_table_offset);
        long audio_off = audio_offset;
        for(audio a : audios){
            fileWriter.writeLong(audio_off);
            audio_off += a.length;
        }
        fileWriter.seek(audio_offset);
        for(audio a : audios){
            byte[] tmp = new byte[(int)a.length];
            fileReader.seek(a.data);
            fileReader.readFully(tmp);
            fileWriter.write(tmp);
        }
    }
    void write_bitmaps() throws Exception {
        fileWriter.seek(bitmap_table_offset);
        for (int index = 0; index < bitmaps.size(); index++) {
            bitmap b = bitmaps.get(index);
            fileWriter.writeLong(bitmap_offset);
            fileReader.seek(b.data);
            int width = readCInt();
            int height = readCInt();
            if (width < 0 || height < 0)
                E("Invalid image size");
            int f1 = readCInt();
            int f2 = fileReader.readUnsignedByte();
            long n1 = fileReader.readUInt();
            if (n1 > 0)
                E("n1 of image not zero");
            int length = (int) fileReader.readUInt();
            int n2 = fileReader.readUnsignedByte();
            if (n2 != 0)
                E("n2 of of image not zero");
            int size = width * height * 4;
            long biggest = Math.max(size, length);
            byte[] input = new byte[(int) biggest];
            byte[] output = new byte[(int) biggest];
            int[] key = b.key;
            int decompressed = 0;
            fileReader.fileReader.read(input, 0, length);
            if ((decompressed = decompress(input, output, length)) == -1 &&
                    ((length = decrypt(input, length, key)) == -1 ||
                            (decompressed = decompress(input, output, length)) == -1)) {
                O.ptEln("Unable to inflate error data");
                f1 = 2;
                f2 = 0;
                decompressed = size;
                fileWriter.write(new byte[size]);
            }
            byte[] tmp = input;
            input = output;
            output = tmp;
            int check = decompressed;
            switch (f1) {
                case 257:
                case 513:
                case 1:
                    check *= 2;
                    break;
                case 2:
                    break;
                case 1026:
                case 2050:
                    check *= 4;
                    break;
                default:
                    E("Unknown image type!");
            }
            int pixels = width * height;
            switch (f2) {
                case 0:
                    break;
                case 4:
                    pixels /= 256;
                    break;
                default:
                    E("Unknown image format");
            }
            if (check != pixels * 4) {
                if (f1 != 2 || f2 != 0)
                    E("Size mismatch");
            }
            switch (f1) {
                case 1:
                    make4444(input, output, pixels);
                    break;
                case 2:
                    tmp = input;
                    input = output;
                    output = tmp;
                    break;
                case 513:
                    make565(input, output, pixels);
                    break;
                case 1026:
                    JSquish.decompressImage(output, width, height, input, JSquish.CompressionType.DXT3);
                    break;
                case 2050:
                    JSquish.decompressImage(output, width, height, input, JSquish.CompressionType.DXT5);
            }
            tmp = input;
            input = output;
            output = tmp;
            switch (f2) {
                case 0:
                    break;
                case 4:
                    O.ptEln("Format 2 of 4");
                    scale(input, output, width, height);
                    tmp = input;
                    input = output;
                    output = tmp;
                    break;
            }
            ByteBuffer bf = BufferUtils.createByteBuffer(length);
            bf.put(input, 0, length);
            bf.position(0);
            ByteBuffer final_out = BufferUtils.createByteBuffer(LZ4.LZ4_compressBound(size));
            int final_size = LZ4.LZ4_compress_default(bf, final_out);
            long nowPos = fileWriter.getFilePointer();
            fileWriter.seek(bitmap_offset);
            fileWriter.writeLong(final_size);
            if (output.length < final_size)
                output = new byte[final_size];
            final_out.get(output, 0, final_size);

            fileWriter.write(output);
            bitmap_offset += 4 + final_size;
            fileWriter.seek(nowPos);
        }
        O.ptln("Well Done!!! All Finished!");
    }
    void scale(byte[] input, byte[] output, int width, int height){
        int w = width / 16;
        int h = height / 16;
        for(int y = 0; y < h; y++){
            for(int x = 0; x < w; x++){
                int p = y * w + x;
                for (int yy = y * 16; yy < (y + 1) * 16; ++yy) {
                    for (int xx = x * 16; xx < (x + 1) * 16; ++xx) {
                        output[yy * width + xx] = input[p];
                        output[yy * width + xx + 1] = input[p + 1];
                        output[yy * width + xx + 2] = input[p + 2];
                        output[yy * width + xx + 3] = input[p + 3];
                    }
                }
            }
        }
    }
    void make565(byte[] input, byte[] output, int pixels) {
        pixels *= 4;
        for (int i = 0; i < pixels; i += 4) {
            output[i] = (byte) (table5[Byte.toUnsignedInt(input[i]) & 31]& 0xFF);
            output[i + 1] = (byte) (table5[Byte.toUnsignedInt(input[i]) >>> 5 + input[1] & 7]& 0xFF);
            output[i + 2] = (byte) (table5[Byte.toUnsignedInt(input[i + 1]) >>> 3]& 0xFF);
            output[i + 3] = (byte)255;
        }
    }
    void make4444(byte[] input, byte[] output, int pixels) {
        pixels *= 4;
        for (int i = 0; i < pixels; i += 4) {
            output[i] = (byte) (table4[Byte.toUnsignedInt(input[i]) & 0xF] & 0xFF);
            output[i + 1] = (byte) (table4[Byte.toUnsignedInt(input[i]) >>> 4]& 0xFF);
            output[i + 2] = (byte) (table4[Byte.toUnsignedInt(input[i + 1]) & 0xF]& 0xFF);
            output[i + 3] = (byte) (table4[Byte.toUnsignedInt(input[i + 1]) >>> 4]& 0xFF);
        }
    }
    int decompress(byte[] input, byte[] output, int length){
        Inflater inflater = new Inflater();
        inflater.setInput(input,0, length);
        int res = 0;
        try {
            res = inflater.inflate(output);
            inflater.end();
            return res;
        } catch (DataFormatException e) {
            return -1;
        } catch (ArrayIndexOutOfBoundsException e2){
            return res;
        }
    }
    int decrypt(byte[] input, int length, int[] key){
        int p = 0;
        byte[] ori = Arrays.copyOf(input, input.length);

        for(int i = 0; i < length - 4; ){
            long blen = Integer.toUnsignedLong((ori[i] & 255) |
                    (Byte.toUnsignedInt(ori[i + 1]) << 8) +
                    (Byte.toUnsignedInt(ori[i + 2]) << 16) +
                    (Byte.toUnsignedInt(ori[i + 3]) << 24));
            i += 4;
            if(i + blen > length)
                return -1;
            for(int j = 0; j < blen; j++){
                input[p + j] = (byte) (ori[i + j] ^ key[j] & 0xFF);
            }
            i += blen;
            p += blen;
        }
        return p;
    }


    public static void main(String[] args) {
        new Convert().convert("D:\\1\\mxd\\冒险岛online\\Sound.wz");
    }
}