package cn.treeh.Game.Data;

import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeMap;

public class ItemData {
    static TreeMap<Integer, ItemData> cache = new TreeMap<>();
    // Creates an item from the game's Item file with the specified id
    public static ItemData get(int itemid){
        if(cache.containsKey(itemid))
            return cache.get(itemid);
        ItemData data = new ItemData(itemid);
        cache.put(itemid, data);
        return data;
    }

    ItemData(int itemid){

        this.itemid = itemid;
        untradable = unique = unsellable = cashitem = false;
        gender = 0;
        Node src = null, str_src = null;
        String str_prefix = "0" + getItemPrefix(itemid);
        String str_id = "0" + itemid;
        int prefix = getPrefix(itemid);
        switch (prefix){
            case 1:
                category = getCategory(itemid);
                src = NXFiles.Character().subNode(category).
                        subNode(str_id + ".img").subNode("info");
                str_src = NXFiles.String().subNode("Eqp.img").
                        subNode("Eqp").subNode(category).
                        subNode("" + itemid);
                break;
            case 2:
                category = "Consume";
                src = NXFiles.Item().subNode("Consume").
                        subNode(str_prefix + ".img").subNode(str_id)
                        .subNode("info");
                str_src =NXFiles.String().subNode("Consume.img")
                        .subNode("" + itemid);
                break;
            case 3:
                category = "Install";
                src = NXFiles.Item().subNode("Install").subNode(str_prefix + ".img").
                        subNode(str_id).subNode("info");
                str_src = NXFiles.String().subNode("Ins.img").subNode("" + itemid);
                break;
            case 4:
                category = "Etc";
                src = NXFiles.Item().subNode("Etc").subNode(str_prefix + ".img").
                        subNode(str_id).subNode("info");
                str_src = NXFiles.String().subNode("Etc.img").subNode("Etc").
                        subNode("" + itemid);
                break;
            case 5:
                category = "Cash";
                src = NXFiles.Item().subNode("Cash").subNode(str_prefix + ".img").
                    subNode(str_id).subNode("info");
                str_src = NXFiles.String().subNode("Cash.img").subNode("" + itemid);
                break;
        }
        if (src != null)
        {
            icons_false= new Texture(src.subNode("icon"));
            icons_true= new Texture(src.subNode("iconRaw"));
            price = src.subNode("price").getInt();
            untradable = src.subNode("tradeBlock").getBool();
            unique = src.subNode("only").getBool();
            unsellable = src.subNode("notSale").getBool();
            cashitem = src.subNode("cash").getBool();
            gender = getItemGender(itemid);

            name = str_src.subNode("name").getString();
            desc = str_src.subNode("desc").getString();

            valid = true;
        }
        else
        {
            valid = false;
        }
    }
    static String[] cats =
    {
        "Cap",
                "Accessory",
                "Accessory",
                "Accessory",
                "Coat",
                "Longcoat",
                "Pants",
                "Shoes",
                "Glove",
                "Shield",
                "Cape",
                "Ring",
                "Accessory",
                "Accessory",
                "Accessory"
    };
    String getCategory(int itemid){
        itemid = getItemPrefix(itemid) - 100;
        if(itemid < 15)
            return cats[itemid];
        else if(itemid >= 30 && itemid <= 70)
            return "Weapon";
        return "";
    }
    int getPrefix(int itemid){
        return itemid / 1000000;
    }
    int getItemPrefix(int itemid){
        return itemid / 10000;
    }
    int getItemGender(int itemid) {

        int item_prefix = getItemPrefix(itemid);

        if ((getPrefix(itemid) != 1 && item_prefix != 254) || item_prefix == 119 || item_prefix == 168)
            return 2;

        int gender_digit = itemid / 1000 % 10;

        return (gender_digit > 1) ? 2 : gender_digit;

    }

    Texture icons_false, icons_true;
    int itemid;
    int price;
    int gender;
    String name;
    String desc;

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    String category;

    boolean valid;
    boolean untradable;
    boolean unique;
    boolean unsellable;
    boolean cashitem;
}
