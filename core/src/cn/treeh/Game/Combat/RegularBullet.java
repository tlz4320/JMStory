package cn.treeh.Game.Combat;

import cn.treeh.Game.Data.BulletData;
import cn.treeh.Game.Player.Char;
import cn.treeh.Graphics.Animation;

public class RegularBullet extends SkillBullet{

    @Override
    public Animation get(Char user, int bulletid) {
        return BulletData.getBulletData(bulletid).get_animation();
    }
}