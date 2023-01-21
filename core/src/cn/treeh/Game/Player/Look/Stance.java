package cn.treeh.Game.Player.Look;

public class Stance {
    public enum Id {
        none,
        alert,
        dead,
        fly,
        heal,
        jump,
        ladder,
        prone,
        proneStab,
        rope,
        shot,
        shoot1,
        shoot2,
        shootF,
        sit,
        stabO1,
        stabO2,
        stabOF,
        stabT1,
        stabT2,
        stabTF,
        stand1,
        stand2,
        swingO1,
        swingO2,
        swingO3,
        swingOF,
        swingP1,
        swingP2,
        swingPF,
        swingT1,
        swingT2,
        swingT3,
        swingTF,
        walk1,
        walk2
    }

    static Id[] values = {Id.walk1, Id.stand1, Id.jump, Id.alert,
            Id.prone, Id.fly, Id.ladder, Id.rope, Id.dead, Id.sit};

    static public Id byState(int state) {
        int index = state / 2 - 1;
        if (index < 0 || index > 10)
            return Id.walk1;
        return values[index];
    }

    static public Id byId(int id) {
        if (id < 0 || id >= Id.values().length)
            return Id.none;
        return Id.values()[id];
    }

    static public Id byString(String name) {
        try {
            return Id.valueOf(name);
        } catch (IllegalArgumentException e) {
            return Id.none;
        }
    }

    static public boolean isClimbing(Id value) {
        return value == Id.ladder || value == Id.rope;
    }

    static public Id baseof(Id value) {
        switch (value) {
            case stand2:
                return Id.stand1;
            case walk2:
                return Id.walk1;
            default:
                return value;
        }
    }

    static public Id secondof(Id value) {
        switch (value) {
            case stand1:
                return Id.stand2;
            case walk1:
                return Id.walk2;
            default:
                return value;
        }
    }
}
