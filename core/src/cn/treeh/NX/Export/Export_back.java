package cn.treeh.NX;

import cn.treeh.Util.O;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.util.*;

public class Export_back {
    public static int[] cvC(ByteBuffer buffer, int len){
        buffer.position(0);
        int[] result = new int[len / 4];
        buffer.asIntBuffer().get(result);
        for(int i = 0; i < result.length; i++) {
            int b = ((result[i] >> 16) & 0xFF);
            int g = ((result[i] >> 8) & 0xFF);
            int r = ((result[i]) & 0xFF);
            int a = (result[i] >> 24) & 0xff;
            result[i] = (a << 24) + (r << 16) + (g << 8) + b;
        }
//        byte[] img = new byte[len];
//        buffer.get(img);
//        int[] result = new int[img.length / 4];
//        for(int i = 0; i < result.length; i++){
//            result[i] =
//        }
        return result;
    }
    static int width = 8192;
    static BufferedImage packer = new BufferedImage(width, width, 3);
    static int now_width = 0;
    static int now_height = 0;
    static int maxHeight = 0, maxWidth = 0;
    static FileWriter writer = null;
    static int id = 0;
    static void addFrame(Node node, String key, int index, LinkedList<Map.Entry<String, int[]>> regions){
        int delay = node.subNode("delay").getInt();
        if (delay == 0)
            delay = 100;
        Bitmap bitmap = node.getBitmap();
        if(bitmap.width + now_width > width){
            now_width = 0;
            now_height += 5 + maxHeight;
            maxHeight = 0;
        }
        maxHeight = Math.max(maxHeight, bitmap.height);
        if(maxHeight + now_height >= width) {
            try {
                ImageIO.write(packer, "PNG", new File("/Users/tobin/Documents/java_project/JMStory/assets/" + idname + id + ".png"));
            }catch (Exception e){

            }
            now_height = now_width = 0;
            packer = new BufferedImage(width, width, 3);
            maxHeight = 0;
            maxWidth = 0;
            id++;
        }
        packer.setRGB(now_width, now_height, bitmap.width, bitmap.height, cvC(bitmap.data(), bitmap.length()), 0, bitmap.width);
        regions.add(new AbstractMap.SimpleEntry<>(key + "_" + index,
                new int[]{now_width, now_height, bitmap.width, bitmap.height, delay, id}));
        now_width += 5 + bitmap.width;
        maxWidth = Math.min(Math.max(maxWidth, now_width), width);
    }
    static void convert(Node node){



        HashMap<String, LinkedList<Map.Entry<String, int[]>>> info = new HashMap<>();
        O.ptln(node.nChild());
        for(int i = 0; i < node.nChild(); i++){
            Node subNode = node.subNode(i);
            if(subNode.nChild() == 0)
                continue;
            if(!info.containsKey(subNode.getName())){
                info.put(subNode.getName(), new LinkedList<>());
            }
            LinkedList<Map.Entry<String, int[]>> regions = info.get(subNode.getName());
            for(int j = 0; j < subNode.nChild(); j++){
                Node subsubNode = subNode.subNode(j);
                if(subsubNode.getType() == Node.Type.bitmap){
                    addFrame(subsubNode, subsubNode.getName(), 0, regions);
                }
                else{

                    if(subsubNode.nChild() > 0){
                        TreeMap<Integer, Integer> order_set = new TreeMap<>();

                        for (int k = 0; k < subsubNode.nChild(); k++) {
                            Node sub = subsubNode.subNode(k);
                            // in order to keep in order
                            // maybe can make it order in convert?
                            if (sub.getType() == Node.Type.bitmap) {
                                order_set.put(Integer.parseInt(sub.getName()), k);
                            }
                        }
                        for (Map.Entry<Integer, Integer> k : order_set.entrySet()) {
                            Node sub = subsubNode.subNode(k.getValue());
                            if(sub.getType() == Node.Type.bitmap){
                                addFrame(sub,subsubNode.getName(), k.getKey(), regions);
                            }
                        }
                    }
                }
            }
        }
        try {
            for (Map.Entry<String, LinkedList<Map.Entry<String, int[]>>> entry : info.entrySet()) {
                for (Map.Entry<String, int[]> entry1 : entry.getValue()) {
                    int[] tmp = entry1.getValue();
                    String name = entry1.getKey();
                    int index = name.lastIndexOf('_');
                    int zig = node.subNode(entry.getKey()).subNode(name.substring(0, index)).subNode("zigzag").getInt();
                    writer.write(node.getName() + "\t" + entry.getKey() + "\t" + name.substring(0, index) + "\t" + name.substring(index + 1) + "\t" +
                            tmp[0] + "\t" + tmp[1] + "\t" + tmp[2] + "\t" + tmp[3] +"\t" +  tmp[4] + "\t" + tmp[5] + "\t" + zig + "\n");
                }
            }
        }catch (Exception e){

        }
    }
static String idname = "Back";
    public static void main(String[] args) {
        try {
            writer = new FileWriter("/Users/tobin/Documents/java_project/JMStory/assets/" + idname + ".txt");
        }catch (Exception e){}
        NxFile map = new NxFile("/Users/tobin/Documents/java_project/JMStory/assets/Map.nx");
        Node node = map.getNode().subNode("Back");
        for (int tt = 0; tt < node.nChild(); tt++) {
            O.ptln(node.subNode(tt).getName());
            convert(node.subNode(tt));
        }
        try {
            packer = packer.getSubimage(0, 0, maxWidth, now_height + maxHeight);
            ImageIO.write(packer, "PNG", new File("/Users/tobin/Documents/java_project/JMStory/assets/" + idname  + id +  ".png"));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
