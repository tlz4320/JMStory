package cn.treeh.NX;

import java.util.HashMap;

public class NXFiles {
    static HashMap<String, NxFile> files;
    static String[] fileName = {
            "UI",
            "Map",
            "Mob",
            "Audio"
    };
    static void init(){
        for(String file : fileName){
            files.put(file + ".nx",
                    new NxFile(file));
        }
    }
}
