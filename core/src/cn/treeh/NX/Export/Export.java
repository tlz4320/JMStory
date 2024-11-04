package cn.treeh.NX;

import cn.treeh.Util.O;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

public class Export {
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
    static void convert(Node node){



        HashMap<String, HashMap<String, int[]>> info = new HashMap<>();
        O.ptln(node.nChild());
        for(int i = 0; i < node.nChild(); i++){
            Node subNode = node.subNode(i);
            if(subNode.nChild() == 0)
                continue;
            if(!info.containsKey(subNode.getName())){
                info.put(subNode.getName(), new HashMap<>());
            }
            HashMap<String, int[]> regions = info.get(subNode.getName());
            for(int j = 0; j < subNode.nChild(); j++){
                Node subsubNode = subNode.subNode(j);
                if(subsubNode.getType() == Node.Type.bitmap){
                    Bitmap bitmap = subsubNode.getBitmap();
                    if(bitmap.width + now_width > width){
                        now_width = 0;
                        now_height += 5 + maxHeight;
                        maxHeight = 0;
                    }
                    maxHeight = Math.max(maxHeight, bitmap.height);
                    packer.setRGB(now_width, now_height, bitmap.width, bitmap.height, cvC(bitmap.data(), bitmap.length()), 0, bitmap.width);
                    int z = subsubNode.subNode("z").getInt();
                    if(z == 0)
                        z = subsubNode.subNode("zM").getInt();
                    int[] origin = subsubNode.subNode("origin").getVector();

                    regions.put(subsubNode.getName(), new int[]{now_width, now_height, bitmap.width, bitmap.height,z, origin[0], origin[1]});
                    now_width += 5 + bitmap.width;
                    maxWidth = Math.min(Math.max(maxWidth, now_width), width);
                }
            }
        }
        try {
            for (Map.Entry<String, HashMap<String, int[]>> entry : info.entrySet()) {
                for (Map.Entry<String, int[]> entry1 : entry.getValue().entrySet()) {
                    int[] tmp = entry1.getValue();
                    writer.write(node.getName() + "\t" + entry.getKey() + "\t" + entry1.getKey() + "\t" +
                            tmp[0] + "\t" + tmp[1] + "\t" + tmp[2] + "\t" + tmp[3]+ "\t" + tmp[4] + "\t" + tmp[5] +"\t" + tmp[6]+"\n");
                }
            }
        }catch (Exception e){

        }
    }

    public static void main(String[] args) {
        try {
            writer = new FileWriter("/Users/tobin/Documents/java_project/JMStory/assets/Tile.txt");
        }catch (Exception e){}
        NxFile map = new NxFile("/Users/tobin/Documents/java_project/JMStory/assets/Map.nx");
        Node node = map.getNode().subNode("Tile");
        for (int tt = 0; tt < node.nChild(); tt++) {
            O.ptln(node.subNode(tt).getName());
            convert(node.subNode(tt));
        }
        try {
            packer = packer.getSubimage(0, 0, maxWidth, now_height + maxHeight);
            ImageIO.write(packer, "PNG", new File("/Users/tobin/Documents/java_project/JMStory/assets/" + node.getName() + ".png"));
            writer.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
