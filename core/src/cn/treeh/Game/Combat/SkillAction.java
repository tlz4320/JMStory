package cn.treeh.Game.Combat;

import cn.treeh.Game.Player.Char;

public abstract class SkillAction {
    abstract public void apply(Char target, Attack.Type atype);
}