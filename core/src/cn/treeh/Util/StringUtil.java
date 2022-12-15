package cn.treeh.Util;

public class StringUtil {
    static public String extend(Object obj, int len){
        String res = obj.toString();
        if(res.length() < len){
            len -= res.length();
            StringBuilder sb = new StringBuilder();
            while(len > 0){
                sb.append("0");
                len--;
            }
            sb.append(res);
            return sb.toString();
        }
        return res;
    }
}
