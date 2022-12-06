package cn.treeh.Util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.IntMap;
import com.github.tommyettinger.textra.Font;
import com.github.tommyettinger.textra.utils.CaseInsensitiveIntMap;

public class FontAddImg {
    public static void fontAddImg(Font font, int index, Texture texture){
        if(font.nameLookup == null)
            font.nameLookup = new CaseInsensitiveIntMap(0xFFFF, 0.5f);
        else
            font.nameLookup.ensureCapacity(0xFFFF);
        if(font.namesByCharCode == null)
            font.namesByCharCode = new IntMap<>(0xFFFF, 0.5f);
        else
            font.namesByCharCode.ensureCapacity(0xFFFF);
        String imgName = "Img" + index;
        int imgIndex = 0xD801 + index;
        font.nameLookup.put(imgName, imgIndex);
        font.namesByCharCode.put(imgIndex, imgName);
        font.mapping.put(imgIndex, new Font.GlyphRegion(new TextureRegion(texture)));
    }
}
