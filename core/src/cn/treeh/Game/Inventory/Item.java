package cn.treeh.Game.Inventory;

public class Item {
    int item_id;
    long expiration;
    String owner;
    int flags;
    public Item(int _id, long _exp, String _o, int _f){
        item_id = _id;
        expiration = _exp;
        owner = _o;
        flags = _f;
    }
}