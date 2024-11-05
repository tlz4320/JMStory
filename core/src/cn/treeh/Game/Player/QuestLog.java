package cn.treeh.Game.Player;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class QuestLog {
    LinkedHashMap<Integer, String> started = new LinkedHashMap<>();
    TreeMap<Integer, Map.Entry<Integer, String>> in_progress = new TreeMap<>();
    TreeMap<Integer, Long> completed = new TreeMap<>();

    public void add_started(int qid,  String qdata)
	{
        started.putLast(qid, qdata);
	}

    public void add_in_progress(int qid, int qidl,  String qdata)
	{
        in_progress.put(qid, new AbstractMap.SimpleEntry<>(qidl, qdata));
	}

    public void add_completed(int qid, long time)
	{
        completed.put(qid, time);
	}

    public boolean is_started(int qid)
	{
		return started.containsKey(qid);
	}

    public int get_last_started()
	{
		Map.Entry<Integer, String> qend = started.lastEntry();
		return qend.getKey();
	}

}