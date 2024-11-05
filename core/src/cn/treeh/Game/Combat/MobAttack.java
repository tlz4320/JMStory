package cn.treeh.Game.Combat;

public class MobAttack {
    Attack.Type type = Attack.Type.CLOSE;
		public int watk = 0;
		int matk = 0;
		int mobid = 0;
		int oid = 0;
		public int[] origin = new int[2];
	   public boolean valid = false;

		// Create a mob attack for touch damage
		public MobAttack(){
            valid = false;
        }
		MobAttack(int watk, int[] origin, int mobid, int oid){
            type = Attack.Type.CLOSE;
            watk = watk;
            origin = origin;
            mobid = mobid;
            oid = oid;
            valid = true;
        }
    public boolean isValid(){
            return valid;
        }
}