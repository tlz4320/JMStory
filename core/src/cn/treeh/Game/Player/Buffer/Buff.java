package cn.treeh.Game.Player.Buffer;

public class Buff {
    BuffStat.Id stat;
    int value;
    int skillid;
    int duration;

    public Buff(BuffStat.Id stat, int value, int skillid, int duration){
        this.stat = stat;
        this.value = value;
        this.skillid = skillid;
        this.duration = duration;
    }
    public Buff(){
        this(BuffStat.Id.NONE, 0, 0, 0);
    }
}