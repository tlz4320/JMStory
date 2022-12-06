//package cn.treeh.Graphics.Text;
//
//import com.badlogic.gdx.graphics.g2d.Batch;
//import com.badlogic.gdx.math.Vector2;
//
//import java.awt.*;
//import java.util.LinkedList;
//
//public class Line {
//    Line(){
//        words = new LinkedList<>();
//    }
//    LinkedList<Words> words;
//    public void addWords(Words w){
//        words.add(w);
//    }
//
//    //绘制完成后会返回最终的宽度和高度
//    //用于计算位置
//    public Vector2 draw(Vector2 pos, Batch batch, String text, boolean underLine){
//        Vector2 res = new Vector2(), tmp, drawPos = pos.cpy();
//        for(Words w : words) {
//            tmp = w.draw(drawPos, batch, text, underLine);
//            drawPos.x += tmp.x;
//            res.x += tmp.x;
//            res.y = Math.max(res.y, tmp.y);
//        }
//        return res;
//    }
//}
