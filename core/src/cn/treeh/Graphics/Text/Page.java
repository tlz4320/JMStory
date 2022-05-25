package cn.treeh.Graphics.Text;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;

import java.util.LinkedList;

public class Page {
    LinkedList<Line> lines;
    public Page(){
        lines = new LinkedList<>();
    }
    public void addLine(Line l){
        lines.add(l);
    }
    public Vector2 draw(Vector2 pos, Batch batch, String text, boolean underLine){
        Vector2 res = new Vector2(), drawPos = pos.cpy(), tmp;
        for(Line line : lines){
            tmp = line.draw(drawPos, batch, text, underLine);
            drawPos.y += tmp.y;
            res.x = Math.max(res.x, tmp.x);
            res.y += tmp.y;
        }
        return res;
    }
}
