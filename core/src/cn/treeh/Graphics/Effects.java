package cn.treeh.Graphics;

import cn.treeh.Util.Configure;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.LinkedList;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;

public class Effects {
    static class Effect{
        Sprite sprite;
        float speed;
        public Effect(Sprite s, float s2){
            sprite = s;
            speed = s2;
        }
        public void draw(int[] pos, float alpha, SpriteBatch batch){
            sprite.draw(pos, alpha, batch);
        }
        public boolean update(){
            return sprite.update((int) (Configure.TIME_STEP * speed));
        }
    }
    TreeMap<Integer, LinkedList<Effect>> effects = new TreeMap<>();
    public void drawBelow(int[] pos, float alpha, SpriteBatch batch){
        for(Map.Entry<Integer, LinkedList<Effect>> e : effects.entrySet()){
            if(e.getKey() == 0)
                break;
            for(Effect effect : e.getValue())
                effect.draw(pos, alpha, batch);
        }
    }
    public void drawAbove(int[] pos, float alpha, SpriteBatch batch){
        for(Map.Entry<Integer, LinkedList<Effect>> e : effects.entrySet()){
            if(e.getKey() < 0)
                continue;
            for(Effect effect : e.getValue())
                effect.draw(pos, alpha, batch);
        }
    }
    public void update(){
        for(LinkedList<Effect> es : effects.values())
            es.removeIf(Effect::update);
    }
    public void add(Animation animation)
	{
		add(animation, new DrawArg(), 0, 1.0f);
	}
    public void add(Animation animation, DrawArg args, int z)
	{
		add(animation, args, z, 1.0f);
	}

	public void add(Animation animation, DrawArg args)
	{
		add(animation, args, 0, 1.0f);
	}
    public void add(Animation animation, DrawArg arg, int z, float speed){
        LinkedList<Effect> tmp = effects.getOrDefault(z, new LinkedList<>());
        tmp.add(new Effect(new Sprite(animation, arg), speed));
        effects.put(z, tmp);

    }
}
