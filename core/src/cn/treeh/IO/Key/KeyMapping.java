package cn.treeh.IO.Key;

public class KeyMapping {
    public KeyType getType() {
        return type;
    }

    public int getAction() {
        return action;
    }

    KeyType type;
    int action;
    public KeyMapping(KeyType i, int a){
        type = i;
        action = a;
    }
    public boolean equals(KeyMapping m){
        return m.action == this.action && m.type == this.type;
    }
    @Override
    public boolean equals(Object o) {
        return o instanceof KeyMapping && equals((KeyMapping) o);
    }
}
