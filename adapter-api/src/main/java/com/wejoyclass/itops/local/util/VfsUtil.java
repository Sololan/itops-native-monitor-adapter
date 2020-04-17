package com.wejoyclass.itops.local.util;

public class VfsUtil {
    public static String toPartition(String temKey){
        if(temKey.contains("1")) return "C";
        if(temKey.contains("2")) return "D";
        if(temKey.contains("3")) return "E";
        if(temKey.contains("4")) return "F";
        if(temKey.contains("5")) return "G";
        if(temKey.contains("6")) return "H";
        if(temKey.contains("7")) return "I";
        if(temKey.contains("8")) return "J";
        return temKey;
    }
}