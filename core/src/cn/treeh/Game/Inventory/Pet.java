package cn.treeh.Game.Inventory;

public class Pet {
    int item_id;
    long expiration;
    String petname;
    int petlevel;
    int closeness;
    int fullness;
    Pet(int item_id, long expiration, String petname, int level, int closeness, int fullness){
        this.item_id = item_id;
        this.expiration = expiration;
        this.petlevel = level;
        this.petname = petname;
        this.closeness = closeness;
        this.fullness = fullness;
    }
}