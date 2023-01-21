package cn.treeh.Game.Player.Look;

import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.O;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.TreeMap;


public class BodyDrawInfo {
    TreeMap<Integer, int[]>[] body_positions = new TreeMap[Stance.Id.values().length];
    TreeMap<Integer, int[]>[] arm_positions = new TreeMap[Stance.Id.values().length];
    TreeMap<Integer, int[]>[] hand_positions = new TreeMap[Stance.Id.values().length];
    TreeMap<Integer, int[]>[] head_positions = new TreeMap[Stance.Id.values().length];
    TreeMap<Integer, int[]>[] hair_positions = new TreeMap[Stance.Id.values().length];
    TreeMap<Integer, int[]>[] face_positions = new TreeMap[Stance.Id.values().length];
    TreeMap<Integer, Integer>[] stance_delays = new TreeMap[Stance.Id.values().length];

    HashMap<String, HashMap<Integer, BodyAction>> body_actions = new HashMap<>();
    HashMap<String, LinkedList<Integer>> attack_delays = new HashMap<>();
    public void init()
    {
        for(int i = 0; i < Stance.Id.values().length; i++) {
            body_positions[i] = new TreeMap<>();
            arm_positions[i] = new TreeMap<>();
            hand_positions[i] = new TreeMap<>();
            head_positions[i] = new TreeMap<>();
            hair_positions[i] = new TreeMap<>();
            face_positions[i] = new TreeMap<>();
            stance_delays[i] = new TreeMap<>();
        }

        Node bodynode = NXFiles.Character().subNode("00002000.img");
        Node headnode = NXFiles.Character().subNode("00012000.img");

        for (int i = 0; i < bodynode.nChild(); i++)
        {
            Node stancenode = bodynode.subNode(i);
            String ststr = stancenode.getName();
            int attackdelay = 0;
            for (int frame = 0; ; ++frame)
            {
                Node framenode = stancenode.subNode("" + frame);
                if(!framenode.valid())
                    break;
                boolean isaction = framenode.subNode("action").getType() == Node.Type.string;

                if (isaction)
                {
                    BodyAction action = new BodyAction(framenode);
                    HashMap<Integer, BodyAction> tmp = body_actions.getOrDefault(ststr, new HashMap<>());
                    tmp.put(frame, action);
                    body_actions.put(ststr, tmp);
                    if (action.isAttack()) {
                        LinkedList<Integer> tmpl = attack_delays.getOrDefault(ststr, new LinkedList<>());
                        tmpl.add(attackdelay);
                        attack_delays.put(ststr, tmpl);
                    }

                    attackdelay += action.getDelay();
                }
                else
                {
                    Stance.Id stance = Stance.byString(ststr);
                    if (stance == Stance.Id.none)
                    {
                        continue;
                    }

                    int delay = framenode.subNode("delay").getInt();

                    if (delay <= 0)
                        delay = 100;

                    stance_delays[stance.ordinal()].put(frame, delay);

                    HashMap<Body.Layer, HashMap<String, int[]>> bodyshiftmap = new HashMap<>();

                    for (int i2 = 0; i2 < framenode.nChild(); i2++)
                    {
                        Node partnode = framenode.subNode(i2);
                        String part = partnode.getName();

                        if (!part.equals("delay") && !part.equals("face"))
                        {
                            String zstr = partnode.subNode("z").getString();
                            Body.Layer z = Body.layers_by_name.get(zstr);
                            if(!bodyshiftmap.containsKey(z))
                                bodyshiftmap.put(z, new HashMap<>());
                            Node map = partnode.subNode("map");
                            for(int i3 = 0; i3 < map.nChild(); i3++){
                                Node mapnode = map.subNode(i3);
                                //非常奇怪，这里不可能出错，但是和Harepack的结果不一致  刚好差1  但是其他的getVector都是正确的
                                //不知道哪里留下的坑
//                                int[] tmp = mapnode.getVector();
//                                tmp[1] += 1;
                                bodyshiftmap.get(z).put(mapnode.getName(), mapnode.getVector());
                            }
                        }
                    }

                    Node headmap = headnode.subNode(ststr).subNode("" + frame).subNode("head/map");
                    for(int i2 = 0; i2 < headmap.nChild(); i2++){
                        Node mapnode = headmap.subNode(i2);
                        if(!bodyshiftmap.containsKey(Body.Layer.HEAD))
                            bodyshiftmap.put(Body.Layer.HEAD, new HashMap<>());
                        bodyshiftmap.get(Body.Layer.HEAD).put(mapnode.getName(), mapnode.getVector());

                    }

                    body_positions[stance.ordinal()].put(frame, bodyshiftmap.getOrDefault(Body.Layer.BODY, new HashMap<>()).
                            getOrDefault("navel", new int[2]));

                    if(bodyshiftmap.containsKey(Body.Layer.ARM) &&
                            bodyshiftmap.get(Body.Layer.ARM).size() > 0) {
                        int[] res = bodyshiftmap.get(Body.Layer.ARM).getOrDefault("hand", new int[2]).clone();
                        int[] tmp = bodyshiftmap.get(Body.Layer.ARM).getOrDefault("navel",  new int[2]);
                        res[0] -= tmp[0];
                        res[1] -= tmp[1];
                        tmp = bodyshiftmap.get(Body.Layer.BODY).getOrDefault("navel", new int[2]);
                        res[0] += tmp[0];
                        res[1] += tmp[1];
                        arm_positions[stance.ordinal()].put(frame, res);
                    }
                    else {
                        int[] res = bodyshiftmap.getOrDefault(Body.Layer.ARM_OVER_HAIR, new HashMap<>()).
                                getOrDefault("hand", new int[2]).clone();
                        int[] tmp = bodyshiftmap.getOrDefault(Body.Layer.ARM_OVER_HAIR, new HashMap<>()).
                                getOrDefault("navel", new int[2]);
                        res[0] -= tmp[0];
                        res[1] -= tmp[1];
                        tmp = bodyshiftmap.getOrDefault(Body.Layer.BODY, new HashMap<>()).
                                getOrDefault("navel", new int[2]);
                        res[0] += tmp[0];
                        res[1] += tmp[1];
                        arm_positions[stance.ordinal()].put(frame, res);
                    }

                    hand_positions[stance.ordinal()].put(frame, bodyshiftmap.
                            getOrDefault(Body.Layer.HAND_BELOW_WEAPON, new HashMap<>()).
                            getOrDefault("handMove", new int[2]));
                    int[] res = bodyshiftmap.getOrDefault(Body.Layer.BODY, new HashMap<>()).
                            getOrDefault("neck", new int[2]).clone();
                    int[] tmp = bodyshiftmap.getOrDefault(Body.Layer.HEAD, new HashMap<>()).
                            getOrDefault("neck", new int[2]);
                    res[0] -= tmp[0];
                    res[1] -= tmp[1];
                    head_positions[stance.ordinal()].put(frame, res);
                    res = bodyshiftmap.getOrDefault(Body.Layer.BODY, new HashMap<>()).
                            getOrDefault("neck", new int[2]).clone();
                    tmp = bodyshiftmap.getOrDefault(Body.Layer.HEAD, new HashMap<>()).
                            getOrDefault("neck", new int[2]);
                    res[0] -= tmp[0];
                    res[1] -= tmp[1];
                    tmp = bodyshiftmap.getOrDefault(Body.Layer.HEAD, new HashMap<>()).
                            getOrDefault("brow", new int[2]);
                    res[0] += tmp[0];
                    res[1] += tmp[1];
                    face_positions[stance.ordinal()].put(frame, res);
                    res = bodyshiftmap.getOrDefault(Body.Layer.HEAD, new HashMap<>()).
                            getOrDefault("brow", new int[2]).clone();
                    tmp = bodyshiftmap.getOrDefault(Body.Layer.HEAD, new HashMap<>()).
                            getOrDefault("neck", new int[2]);
                    res[0] -= tmp[0];
                    res[1] -= tmp[1];
                    tmp = bodyshiftmap.getOrDefault(Body.Layer.BODY, new HashMap<>()).
                            getOrDefault("neck", new int[2]);
                    res[0] += tmp[0];
                    res[1] += tmp[1];
                    hair_positions[stance.ordinal()].put(frame, res);
                }
            }
        }
    }

    int[] getBodyPos(Stance.Id stance, int frame)
    {
        return body_positions[stance.ordinal()].getOrDefault(frame, new int[2]).clone();
    }

    int[] getArmPos(Stance.Id stance, int frame)
    {
        return arm_positions[stance.ordinal()].getOrDefault(frame, new int[2]).clone();
    }

    int[] getHandPos(Stance.Id stance, int frame)
    {
        return  hand_positions[stance.ordinal()].getOrDefault(frame, new int[2]).clone();

    }

    int[] getHeadPos(Stance.Id stance, int frame)
    {
        return head_positions[stance.ordinal()].getOrDefault(frame, new int[2]).clone();
    }

    int[] getHairPos(Stance.Id stance, int frame) 
    {
        return hair_positions[stance.ordinal()].getOrDefault(frame, new int[2]).clone();
    }

    int[] getFacePos(Stance.Id stance, int frame) 
    {
        return face_positions[stance.ordinal()].getOrDefault(frame, new int[2]).clone();
    }

    int nextframe(Stance.Id stance, int frame)
    {
        if (stance_delays[stance.ordinal()].containsKey(frame + 1))
            return frame + 1;
        else
            return 0;
    }

    int getDelay(Stance.Id stance, int frame)
    {
        return stance_delays[stance.ordinal()].getOrDefault(frame, 0);
    }

    int getAttackDelay(String action, int no)
    {
        LinkedList<Integer> action_iter = attack_delays.get(action);
        if(action_iter != null && action_iter.size() > no)
            return action_iter.get(no);
        return 0;
    }

    int next_actionframe(String action, int frame)
    {
        HashMap<Integer, BodyAction> action_iter = body_actions.get(action);
        if(action_iter != null && action_iter.containsKey(frame + 1)){
            return frame + 1;
        }
        return 0;
    }

	 BodyAction getAction(String action, int frame)
    {
        HashMap<Integer, BodyAction> action_iter = body_actions.get(action);
        if(action_iter != null && action_iter.containsKey(frame))
            return action_iter.get(frame);
        return null;
    }
}
