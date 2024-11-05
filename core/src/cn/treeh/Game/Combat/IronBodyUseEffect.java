package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;

public class IronBodyUseEffect extends SkillUseEffect {

    @Override
    public void apply(Char target) {
        target.showIronBody();        
    }
}