package cn.treeh.Game.Inventory;

public class InventoryType {
    public enum Id
            {
                NONE,
                EQUIP,
                USE,
                SETUP,
                ETC,
                CASH,
                DEC,
                EQUIPPED,
                LENGTH
            }
    static Id[] values_by_id = new Id[]{
            Id.NONE,Id.EQUIP,Id.USE,Id.SETUP,Id.ETC,Id.CASH,Id.DEC};
    // Return the inventory type by item id
    public static Id by_item_id(int item_id){


        int prefix = item_id / 1000000;

        return (prefix > 0 && prefix < values_by_id.length) ? values_by_id[prefix] : Id.NONE;
    }
    // Return the inventory type by value
    static Id by_value(int value){
        switch (value)
        {
            case -1:
                return Id.EQUIPPED;
                case 1:
                    return Id.EQUIP;
                    case 2:
                        return Id.USE;
                        case 3:
                            return Id.SETUP;
                            case 4:
                                return Id.ETC;
                                case 5:
                                    return Id.CASH;
                                    case 6:
                                        return Id.DEC;
        }
        return Id.NONE;
    }
}
