package cn.treeh.Game.Player.Look;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.TreeMap;

public class Hair {
    public enum Layer
    {
        NONE,
        DEFAULT,
        BELOWBODY,
        OVERHEAD,
        SHADE,
        BACK,
        BELOWCAP,
        BELOWCAPNARROW,
        BELOWCAPWIDE,
        NUM_LAYERS
    }

    static HashMap<String, Hair.Layer> layers_by_name = new HashMap<>();
    static {
        layers_by_name.put("hair", Hair.Layer.DEFAULT);
        layers_by_name.put("hairBelowBody", Hair.Layer.BELOWBODY);
        layers_by_name.put("hairOverHead", Hair.Layer.OVERHEAD);
        layers_by_name.put("hairShade", Hair.Layer.SHADE);
        layers_by_name.put("backHair", Hair.Layer.BACK);
        layers_by_name.put("backHairBelowCap", Hair.Layer.BELOWCAP);
        layers_by_name.put("backHairBelowCapNarrow", Hair.Layer.BELOWCAPNARROW);
        layers_by_name.put("backHairBelowCapWide", Hair.Layer.BELOWCAPWIDE);
    }
    String name;
    String color;
    static String[] haircolors =
    {
        "Black", "Red", "Orange", "Blonde", "Green", "Blue", "Violet", "Brown"
    };
    TreeMap<Integer, Texture>[][] stances =
            new TreeMap[Stance.Id.values().length][Layer.NUM_LAYERS.ordinal()];
    public Hair(int hair, BodyDrawInfo drawInfo){
        for(int i = 0; i < Stance.Id.values().length; i++){
            for(int j = 0; j < Layer.NUM_LAYERS.ordinal(); j++){
                    stances[i][j] = new TreeMap<>();
            }
        }
        Node hairNode = NXFiles.Character().subNode("Hair")
                .subNode("000" + hair + ".img");
        for(Stance.Id stance : Stance.Id.values()){
            Node stanceNode = hairNode.subNode(stance.name());
            if(!stanceNode.valid())
                continue;
            for(int frame = 0; ; frame++){
                Node frameNode = stanceNode.subNode("" + frame);
                if(!frameNode.valid())
                    break;
                for(int i = 0; i < frameNode.nChild(); i++){
                    Node layerNode = frameNode.subNode(i);
                    String layerName = layerNode.getName();
                    Layer layer = layers_by_name.get(layerName);
                    if(layer == null)
                        continue;
                    int[] brow = layerNode.subNode("map/brow").getVector();
                    int[] shift = drawInfo.getHairPos(stance, frame);
                    shift[0] -= brow[0];
                    shift[1] -= brow[1];
                    Texture texture = new Texture(layerNode);
                    texture.shift(shift);
                    if(texture.isValid()){
                        stances[stance.ordinal()][layer.ordinal()].put(
                              frame, texture
                        );
                        continue;
                    }
                    String defaultstancename = "default";
                    if(layerName.substring(0, 4).equals("back"))
                        defaultstancename = "backDefault";
                    Node defaultNode = hairNode.subNode(defaultstancename)
                            .subNode(layerName);
                    texture = new Texture(defaultNode);
                    texture.shift(shift);
                    if(texture.isValid()){
                        stances[stance.ordinal()][layer.ordinal()].put(frame, texture);
                        continue;
                    }
                    Node defaultNode2 = defaultNode.subNode("0");
                    texture = new Texture(defaultNode2);
                    texture.shift(shift);
                    if(texture.isValid()){
                        stances[stance.ordinal()][layer.ordinal()].put(frame, texture);
                    }
                }
            }
        }
        name = NXFiles.String().subNode("Eqp.img/Eqp/Hair")
                .subNode("" + hair).subNode("name").getString();
        hair = hair % 10;
        color = hair < 8 ? haircolors[hair] : "";
    }
    public void draw(Layer layer, Stance.Id stance, int frame,
                     DrawArg arg, SpriteBatch batch){
        Texture texture = stances[stance.ordinal()][layer.ordinal()] != null ?
                stances[stance.ordinal()][layer.ordinal()].get(frame) : null;
        if(texture != null)
            texture.draw(arg, batch);
    }
    public String getName(){
        return name;
    }
    public String getColor(){
        return color;
    }
}
