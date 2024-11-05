package cn.treeh.Game.Player.Look;

import cn.treeh.Graphics.DrawArg;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.HashMap;
import java.util.TreeMap;

public class CharEquips {
    public enum CapType {
        NONE,
        HEADBAND,
        HAIRPIN,
        HALFCOVER,
        FULLCOVER
    }

    HashMap<EquipSlot.Id, Clothing> clothes = new HashMap<>();

    TreeMap<Integer, Clothing> cloth_cache = new TreeMap<>();

    public CharEquips() {

    }

    public void draw(EquipSlot.Id slot, Stance.Id stance, Clothing.Layer layer, int frame, DrawArg args, SpriteBatch batch) {
        if (clothes.containsKey(slot))
            clothes.get(slot).draw(stance, layer, frame, args, batch);
    }
    public void remove_equip(EquipSlot.Id slot)
	{
        clothes.remove(slot);
	}
    public void addEquip(int itemId, BodyDrawInfo info) {
        if (itemId <= 0)
            return;
        Clothing clothing = cloth_cache.get(itemId);
        if (clothing == null) {
            clothing = cloth_cache.put(itemId, new Clothing(itemId, info));
        }
        clothes.put(clothing.getSlot(), clothing);
    }

    public Stance.Id adjustStance(Stance.Id stance) {
        Clothing weapon = clothes.get(EquipSlot.Id.WEAPON);
        if (weapon != null) {
            switch (stance) {
                case stand1:
                case stand2:
                    return weapon.getStand();
                case walk1:
                case walk2:
                    return weapon.getWalk();
                default:
                    return stance;
            }
        }
        return stance;
    }

    public CapType getcaptype() {
        Clothing cap = clothes.get(EquipSlot.Id.HAT);
        if (cap != null) {
            String vslot = cap.vslot;
            switch (vslot) {
                case "CpH1H5":
                    return CapType.HALFCOVER;
                case "CpH1H5AyAs":
                    return CapType.FULLCOVER;
                case "CpH5":
                    return CapType.HEADBAND;
                default:
                    return CapType.NONE;
            }
        } else {
            return CapType.NONE;
        }
    }

    public int getEquip(EquipSlot.Id slot) {
        Clothing cloth = clothes.get(slot);
        if (cloth != null)
            return cloth.itemid;
        else
            return 0;
    }

    public boolean hasOverall() {
        return getEquip(EquipSlot.Id.TOP) / 10000 == 105;
    }

    public boolean isTwohanded() {
        Clothing weapon = clothes.get(EquipSlot.Id.WEAPON);
        if (weapon != null)
            return weapon.twohanded;
        else
            return false;
    }

	public int getWeapon()
	{
		return getEquip(EquipSlot.Id.WEAPON);
	}

    public boolean hasWeapon()
	{
		return getWeapon() != 0;
	}
}
