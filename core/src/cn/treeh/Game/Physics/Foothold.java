package cn.treeh.Game.Physics;

import cn.treeh.NX.Node;

public class Foothold {
    int m_id;
    int m_prev;
    int m_next;
    int m_layer;
    boolean fallDown;
    int[] m_horizontal;
    int[] m_vertical;
    public Foothold(){
        m_id = m_layer = m_next = m_prev = 0;
        m_horizontal = new int[2];
        m_vertical = new int[2];
    }
    public Foothold(Node src, int id, int layer){
        m_prev = src.subNode("prev").getInt();
        m_next = src.subNode("next").getInt();
        m_horizontal = new int[]{src.subNode("x1").getInt(), src.subNode("x2").getInt()};
        m_vertical = new int[]{src.subNode("y1").getInt(), src.subNode("y2").getInt()};
        fallDown = src.subNode("forbidFallDown").getBool(true);
        m_id = id;
        m_layer = layer;
    }
    public int id()
    {
        return m_id;
    }

    public int prev()
    {
        return m_prev;
    }

    public int next() 
    {
        return m_next;
    }

    public int layer() 
    {
        return m_layer;
    }

	public int[] horizontal() 
    {
        return m_horizontal;
    }

    public int[] vertical() 
    {
        return m_vertical;
    }

    public int l() 
    {
        return Math.min(m_horizontal[0], m_horizontal[1]);
    }

    public int r() 
    {
        return Math.max(m_horizontal[0], m_horizontal[1]);
    }

    public int t() 
    {
        return Math.min(m_vertical[0], m_vertical[1]);
    }

    public int b() 
    {
        return Math.max(m_vertical[0], m_vertical[1]);
    }

    public int x1() 
    {
        return m_horizontal[0];
    }

    public int x2() 
    {
        return m_horizontal[1];
    }

    public int y1() 
    {
        return m_vertical[0];
    }

    public int y2() 
    {
        return m_vertical[1];
    }

    boolean is_wall() 
    {
        return m_id != 0 && (m_horizontal[0] == m_horizontal[1]);
    }

    boolean is_floor() 
    {
        return m_id != 0 && (m_vertical[0] == m_vertical[1]);
    }

    boolean is_left_edge() 
    {
        return m_id != 0 && m_prev == 0;
    }

    boolean is_right_edge() 
    {
        return m_id != 0 && m_next == 0;
    }
    public boolean contain(int[] r, int x){
        //原来代码就是这么写的  我怀疑是错的 不然前面加这么多max和min干啥
        return x >= r[0] && x <= r[1];
    }
    boolean hcontains(int x)
    {
        return m_id != 0 && contain(m_horizontal, x);
    }

    boolean vcontains(int y)
    {
        return m_id != 0 && contain(m_vertical, y);
    }
    public boolean overlap(int[] a, int[] b){
        return a[0] < b[1] && a[1] > b[0];
    }
    boolean is_blocking(int[] vertical)
    {
        return is_wall() && overlap(m_vertical, vertical);
    }

    public int hdelta() 
    {
        return m_horizontal[1] - m_horizontal[0];
    }

    public int vdelta() 
    {
        return m_vertical[1] - m_vertical[0];
    }

    double slope() 
    {
        return is_wall() ? 0.0f : (double)vdelta() / hdelta();
    }

    double ground_below(double x) 
    {
        return is_floor() ? y1() : slope() * (x - x1()) + y1();
    }
}
