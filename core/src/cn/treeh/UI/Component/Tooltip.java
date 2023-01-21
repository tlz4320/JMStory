package cn.treeh.UI.Component;

public interface Tooltip {
    public enum Parent
    {
        NONE,
        EQUIPINVENTORY,
        ITEMINVENTORY,
        SKILLBOOK,
        SHOP,
        EVENT,
        TEXT,
        KEYCONFIG,
        WORLDMAP,
        MINIMAP
    };

    public void draw(int[] cursorpos);
}
