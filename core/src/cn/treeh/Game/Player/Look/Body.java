package cn.treeh.Game.Player.Look;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.O;
import cn.treeh.Util.StringUtil;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.Objects;
import java.util.TreeMap;

public class Body {
    enum Layer {
        NONE,
        BODY,
        ARM,
        ARM_BELOW_HEAD,
        ARM_BELOW_HEAD_OVER_MAIL,
        ARM_OVER_HAIR,
        ARM_OVER_HAIR_BELOW_WEAPON,
        HAND_BELOW_WEAPON,
        HAND_OVER_HAIR,
        HAND_OVER_WEAPON,
        EAR,
        HEAD,
        HIGH_LEF_EAR,
        HUMAN_EAR,
        LEF_EAR,
        NUM_LAYERS
    }

    TreeMap<Integer, Texture>[][] stances = new TreeMap[Stance.Id.values().length][Layer.values().length];
    String name;

    public Body(int skin, BodyDrawInfo drawInfo) {
        for (int i = 0; i < Stance.Id.values().length; i++) {
            for (int j = 0; j < Layer.values().length; j++) {
                stances[i][j] = new TreeMap<>();
            }
        }
        String strid = StringUtil.extend(skin, 2);
        Node bodynode = NXFiles.Character().subNode("000020" + strid + ".img");
        Node headnode = NXFiles.Character().subNode("000120" + strid + ".img");
        for (Stance.Id stance : Stance.Id.values()) {
            String stancename = stance.name();
            Node stancenode = bodynode.subNode(stancename);
            if (!stancenode.valid())
                continue;
            for (int frame = 0; true; frame++) {
                Node framenode = stancenode.subNode("" + frame);
                if (!framenode.valid())
                    break;
                for (int i = 0; i < framenode.nChild(); i++) {
                    Node partnode = framenode.subNode(i);
                    String part = partnode.getName();
                    if (!part.equals("delay") && !part.equals("face")) {
                        String z = partnode.subNode("z").getString();
                        Layer layer = layersByName(z);
                        if (layer == Layer.NONE)
                            continue;
                        int[] shift;
                        int[] tmp;
                        if (Objects.requireNonNull(layer) == Layer.HAND_BELOW_WEAPON) {
                            shift = drawInfo.getHandPos(stance, frame);
                            tmp = partnode.subNode("map").subNode("handMove").getVector();
                            shift[0] -= tmp[0];
                            shift[1] -= tmp[1];
                        } else {
                            shift = drawInfo.getBodyPos(stance, frame);
                            tmp = partnode.subNode("map").subNode("navel").getVector();
                            shift[0] -= tmp[0];
                            shift[1] -= tmp[1];
                        }
                        Texture t = new Texture(partnode);
                        t.shift(shift);
                        stances[stance.ordinal()][layer.ordinal()].put(frame, t);
                    }
                }
                Node tmpnode = headnode.subNode(stancename).subNode("" + frame);
                for (int i = 0; i < tmpnode.nChild(); i++) {
                    Node partnode = tmpnode.subNode(i);
                    String part = partnode.getName();
                    Layer layer = layersByName(part);
                    if (layer == Layer.NONE)
                        continue;
                    int[] shift = drawInfo.getHeadPos(stance, frame);
                    Texture t = new Texture(partnode);
                    t.shift(shift);
                    stances[stance.ordinal()][layer.ordinal()].put(frame, t);
                }
            }
        }

        if (skin < skintypes.length)
            name = skintypes[skin];
        if(name.equals("")){
            name = NXFiles.String().subNode("Eqp.img/Eqp/Skin").subNode(skin).subNode("name").getString();
        }
    }
    public void draw(Layer layer, Stance.Id stance, int frame, DrawArg args, SpriteBatch batch)
    {
        Texture frameit = stances[stance.ordinal()][layer.ordinal()].get(frame);

        if (frameit == null)
            return;

        frameit.draw(args, batch);
    }
    public Layer layersByName(String name) {
        return layers_by_name.getOrDefault(name, Layer.NONE);
    }

    static String[] skintypes =
            {
                    "Light",
                    "Tan",
                    "Dark",
                    "Pale",
                    "Ashen",
                    "Green",
                    "",
                    "",
                    "",
                    "Ghostly",
                    "Pale Pink",
                    "Clay",
                    "Alabaster"
            };
    static public HashMap<String, Layer> layers_by_name = new HashMap<>();

    static {
        layers_by_name.put("body", Layer.BODY);
        layers_by_name.put("bodyBody", Layer.BODY);
        layers_by_name.put("arm", Layer.ARM);
        layers_by_name.put("armBelowHead", Layer.ARM_BELOW_HEAD);
        layers_by_name.put("armBelowHeadOverMailChest", Layer.ARM_BELOW_HEAD_OVER_MAIL);
        layers_by_name.put("armOverHair", Layer.ARM_OVER_HAIR);
        layers_by_name.put("armOverHairBelowWeapon", Layer.ARM_OVER_HAIR_BELOW_WEAPON);
        layers_by_name.put("handBelowWeapon", Layer.HAND_BELOW_WEAPON);
        layers_by_name.put("handOverHair", Layer.HAND_OVER_HAIR);
        layers_by_name.put("handOverWeapon", Layer.HAND_OVER_WEAPON);
        layers_by_name.put("ear", Layer.EAR);
        layers_by_name.put("head", Layer.HEAD);
        layers_by_name.put("highlefEar", Layer.HIGH_LEF_EAR);
        layers_by_name.put("humanEar", Layer.HUMAN_EAR);
        layers_by_name.put("lefEar", Layer.LEF_EAR);
    }
}
