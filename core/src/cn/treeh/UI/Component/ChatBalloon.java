package cn.treeh.UI.Component;

import cn.treeh.Graphics.DrawArg;
import cn.treeh.Graphics.Text;
import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.Configure;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.github.tommyettinger.textra.TypingLabel;

public class ChatBalloon {
    static int DURATION = 4000; // 4 seconds

    MapleFrame frame;
    TypingLabel textlabel;
    Texture arrow;
    int duration;
    public ChatBalloon(int type)
    {
        String typestr = "" + type;

        if (type < 0)
        {
            switch (type)
            {
                case -1:
                    typestr = "dead";
                    break;
            }
        }

        Node src = NXFiles.UI().subNode("ChatBalloon.img")
            .subNode("" + typestr);

        arrow = new Texture(src.subNode("arrow"));
        frame = new MapleFrame(src);

        textlabel = new TypingLabel("", Text.texraFont);

        duration = 0;
    }
    public ChatBalloon(){
        this(0);
    }
    public void changeText(String text)
    {
        textlabel.setText(text);
        duration = DURATION;
    }

    public void draw(int[] position, SpriteBatch batch)
    {
        if (duration == 0)
            return;

        int width = (int)textlabel.getWidth();
        int height = (int)textlabel.getHeight();

        frame.draw(position, width, height, batch);
        arrow.draw(new DrawArg(position), batch);
        textlabel.setPosition(position[0], position[1] - height - 4);
        textlabel.draw(batch, 1.0f);
    }
    public void update()
    {
        duration -= Configure.TIME_STEP;

        if (duration < 0)
            duration = 0;
    }
}
