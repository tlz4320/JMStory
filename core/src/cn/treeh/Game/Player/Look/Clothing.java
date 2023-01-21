package cn.treeh.Game.Player.Look;

import cn.treeh.Game.Data.EquipData;
import cn.treeh.Game.Data.WeaponData;
import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class Clothing {
    enum Layer
    {
        CAPE,
        SHOES,
        PANTS,
        TOP,
        MAIL,
        MAILARM,
        EARRINGS,
        FACEACC,
        EYEACC,
        PENDANT,
        BELT,
        MEDAL,
        RING,
        CAP,
        CAP_BELOW_BODY,
        CAP_OVER_HAIR,
        GLOVE,
        WRIST,
        GLOVE_OVER_HAIR,
        WRIST_OVER_HAIR,
        GLOVE_OVER_BODY,
        WRIST_OVER_BODY,
        SHIELD,
        BACKSHIELD,
        SHIELD_BELOW_BODY,
        SHIELD_OVER_HAIR,
        WEAPON,
        BACKWEAPON,
        WEAPON_BELOW_ARM,
        WEAPON_BELOW_BODY,
        WEAPON_OVER_HAND,
        WEAPON_OVER_BODY,
        WEAPON_OVER_GLOVE,
        NUM_LAYERS
    }
    static Clothing.Layer[] layers =
    {
        Clothing.Layer.CAP,
            Clothing.Layer.FACEACC,
            Clothing.Layer.EYEACC,
            Clothing.Layer.EARRINGS,
            Clothing.Layer.TOP,
            Clothing.Layer.MAIL,
            Clothing.Layer.PANTS,
            Clothing.Layer.SHOES,
            Clothing.Layer.GLOVE,
            Clothing.Layer.SHIELD,
            Clothing.Layer.CAPE,
            Clothing.Layer.RING,
            Clothing.Layer.PENDANT,
            Clothing.Layer.BELT,
            Clothing.Layer.MEDAL
    };
    HashMap<Stance.Id, HashMap<Layer, TreeMap<Integer, Texture>>> stances = new HashMap<>();
    int  itemid;
    EquipSlot.Id slot;

    public Stance.Id getWalk() {
        return walk;
    }

    public Stance.Id getStand() {
        return stand;
    }

    Stance.Id walk;
    Stance.Id stand;
    String vslot;
    boolean twohanded;
    boolean transparent;

    static HashMap<String, Layer> sublayernames = new HashMap<>();
    public Clothing(int id, BodyDrawInfo drawinfo){
        EquipData equipData = EquipData.get(itemid);
        slot = equipData.getSlot();
        twohanded = slot == EquipSlot.Id.WEAPON && WeaponData.get(itemid).isTwohanded();
        int NON_WEAPON_TYPES = 15;
        int WEAPON_OFFSET = NON_WEAPON_TYPES + 15;
        int WEAPON_TYPES = 20;
        Layer chlayer;
        int index = (itemid / 10000) - 100;

        if (index < NON_WEAPON_TYPES)
            chlayer = layers[index];
        else if (index >= WEAPON_OFFSET && index < WEAPON_OFFSET + WEAPON_TYPES)
            chlayer = Clothing.Layer.WEAPON;
		else
        chlayer = Clothing.Layer.CAPE;

        String strid = "0" + itemid;
        String category = equipData.getItemdata().getCategory();
        Node src = NXFiles.Character().subNode(category).subNode(strid + ".img");
        Node info = src.subNode("info");

        vslot = info.subNode("vslot").getString();
        int standno = info.subNode("stand").getInt();
        switch (standno)
        {
            case 1:
                stand = Stance.Id.stand1;
                break;
            case 2:
                stand = Stance.Id.stand2;
                break;
            default:
                stand = twohanded ? Stance.Id.stand2 : Stance.Id.stand1;
                break;
        }
        int walkno = info.subNode("walk").getInt();
        switch (walkno)
        {
            case 1:
                walk = Stance.Id.walk1;
                break;
            case 2:
                walk = Stance.Id.walk2;
                break;
            default:
                walk = twohanded ? Stance.Id.walk2 : Stance.Id.walk1;
                break;
        }

        for (Stance.Id stance : Stance.values)
        {
            String stancename = stance.name();

            Node stancenode = src.subNode(stancename);

            if (!stancenode.valid())
                continue;

            for (int frame = 0;; ++frame)
            {
                Node framenode = stancenode.subNode(frame);
                if(!framenode.valid())
                    break;

                for (int i = 0; i < framenode.nChild(); i++)

                {
                    Node partnode  = framenode.subNode(i);
                    String part = partnode.getName();

                    if (!partnode.valid() || partnode.getType() != Node.Type.bitmap)
                        continue;

                    Clothing.Layer z = chlayer;
                    String zs = partnode.subNode("z").getString();

                    if (part.equals("mailArm"))
                    {
                        z = Clothing.Layer.MAILARM;
                    }
                    else
                    {
                        Layer sublayer_iter = sublayernames.get(zs);
                        if (sublayer_iter != null)
                            z = sublayer_iter;
                    }

                    String parent = "";
                    int[] parentpos = new int[2];
                    Node partNodeMap = partnode.subNode("map");
                    for (int i2 = 0; i2 < partNodeMap.nChild(); i2++)
                    {
                        Node mapnode = partNodeMap.subNode(i2);
                        if (mapnode.getType() == Node.Type.vector)
                        {
                            parent = mapnode.getName();
                            parentpos = mapnode.getVector();
                        }
                    }

                    int[] shift = new int[2];

                    switch (slot)
                    {
                        case FACE:
                        {
                            shift[0] -= parentpos[0];
                            shift[1] -= parentpos[1];
                            break;
                        }
                        case SHOES:
                        case GLOVES:
                        case TOP:
                        case BOTTOM:
                        case CAPE:
                        {
                            shift = drawinfo.getBodyPos(stance, frame);
                            shift[0] -= parentpos[0];
                            shift[1] -= parentpos[1];
                            break;
                        }
                        case HAT:
                        case EARACC:
                        case EYEACC:
                        {
                            shift = drawinfo.getFacePos(stance, frame);
                            shift[0] -= parentpos[0];
                            shift[1] -= parentpos[1];
                            break;
                        }
                        case SHIELD:
                        case WEAPON:
                        {
                            switch (parent) {
                                case "handMove":
                                    shift = drawinfo.getHandPos(stance, frame);
                                    break;
                                case "hand":
                                    shift = drawinfo.getArmPos(stance, frame);
                                    break;
                                case "navel":
                                    shift = drawinfo.getBodyPos(stance, frame);
                                    break;
                            }

                            shift[0] -= parentpos[0];
                            shift[1] -= parentpos[1];
                            break;
                        }
                    }

                    HashMap<Layer, TreeMap<Integer, Texture>> tmpMap = stances.getOrDefault(stance, new HashMap<>());
                    stances.put(stance, tmpMap);
                    TreeMap<Integer, Texture> tm = tmpMap.getOrDefault(z, new TreeMap<>());
                    tmpMap.put(z, tm);
                    Texture texture = new Texture(partnode);
                    texture.shift(shift);
                    tm.put(frame, texture);
                }
            }
        }
        transparent = transparents.contains(itemid);
    }
    static HashSet<Integer> transparents = new HashSet<>();
    static {
        transparents.add(1002186);
    }
    EquipSlot.Id getSlot()
    {
        return slot;
    }
    public void draw(Stance.Id stance, Clothing.Layer layer, int frame, DrawArg args, SpriteBatch batch){}
}
