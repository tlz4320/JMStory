package cn.treeh.Graphics;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class TButton extends TextButton {

    public TButton(String text, Skin skin) {
        super(text, skin);
    }

    public TButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public TButton(String text, TextButtonStyle style) {
        super(text, style);
    }
}
