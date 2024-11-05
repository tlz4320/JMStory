package cn.treeh.Game.Data;

import cn.treeh.Graphics.Texture;
import cn.treeh.NX.NXFiles;
import cn.treeh.NX.Node;
import cn.treeh.Util.StringUtil;

import java.util.LinkedList;
import java.util.TreeMap;

public class JobData {
    static TreeMap<Integer, JobData> cache = new TreeMap<>();


    Texture icon;
    LinkedList<Integer> skills = new LinkedList<>();
    String name;
    static public JobData getJobData(int id){
        if(cache.containsKey(id)){
            return cache.get(id);
        }
        JobData jd = new JobData(id);
        cache.put(id, jd);
        return jd;
    }
    private JobData(int id)
    {
        String strid = StringUtil.extend(id, 3);
        Node src = NXFiles.Skill().subNode(strid + ".img");
        Node strsrc = NXFiles.String().subNode("Skill.img").subNode(strid);

        icon = new Texture(src.subNode("info").subNode("icon"));

        name = strsrc.subNode("bookName").getString();
        Node sk = src.subNode("skill");
        int nChild = sk.nChild();
        for (int i = 0; i < nChild; i++)
        {
            Node sub = sk.subNode(i);
            int skill_id = 0;
            try{skill_id = Integer.parseInt(sub.getName());
            } catch (NumberFormatException ignore){}

            if (skill_id == 0)
                continue;

            skills.add(skill_id);
        }
    }

    public LinkedList<Integer> get_skills()
    {
        return skills;
    }

    public String get_name()
    {
        return name;
    }

    public Texture get_icon() {
        return icon;
    }
}