package cn.treeh.UI.Component;

import cn.treeh.Graphics.Text;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class TextInput extends TextField {

    public static TextInput createTextInput(String text, BitmapFont font){
        TextureRegion solid = Text.texraFont.mapping.get(Text.texraFont.solidBlock);
        Drawable pipe = new TextureRegionDrawable(solid).tint(Color.BLACK),
                selection = new TextureRegionDrawable(solid).tint(Color.GRAY),
                background = new TextureRegionDrawable(solid).tint(Color.CLEAR);
        pipe.setMinWidth(1);
        background.setMinHeight(1);
        background.setMinWidth(1);
        return new TextInput(text, new TextFieldStyle(font, Color.BLACK, pipe, selection, background));
    }

    public TextInput(String text, TextFieldStyle style) {
        super(text, style);
    }

    @Override
    public void setStyle(TextFieldStyle style) {
        super.setStyle(style);
        textHeight = 0;
    }

    @Override
    protected void drawCursor(Drawable cursorPatch, Batch batch, BitmapFont font, float x, float y) {
        cursorPatch.draw(batch,
                x + textOffset + glyphPositions.get(cursor) - glyphPositions.get(visibleTextStart) + fontOffset + font.getData().cursorX,
                y - 10 - font.getDescent(), cursorPatch.getMinWidth(), 20);
    }
}
