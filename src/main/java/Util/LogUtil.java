package Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtil {
    public static final String error = "ERR";
    public static final String warning = "WAR";
    public static final String info = "INFO";
    private String tag = "LOG";
    public LogUtil(String tagName){
        this.tag = tagName;
    }
    public static void E(String tag,String message){
        System.out.println(getCurFormatDate() + " :: "+"["+(char)27 + "[31mERR" + (char)27 + "[0m]"+"-["+tag+"]"+" ::: "+ message);
    }
    public void e(String message){
        E(tag,message);
    }

    public static void W(String tag,String message){
        System.out.println(getCurFormatDate() + " :: "+"["+(char)27 + "[33mWAR" + (char)27 + "[0m]"+"-["+tag+"]"+" ::: "+ message);
    }
    public void w(String message){
        W(tag,message);
    }

    public static void I(String tag,String message){
        System.out.println(getCurFormatDate() + " :: "+"["+(char)27 + "[34mINFO" + (char)27 + "[0m]"+"-["+tag+"]"+" ::: "+ message);
    }
    public void i(String message){
        I(tag,message);
    }

    private static String getCurFormatDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd HH:mm:ss:SSS");
        return simpleDateFormat.format(System.currentTimeMillis());

    }
}
