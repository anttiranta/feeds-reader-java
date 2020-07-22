package com.antti.task.util;

public class Strings {
    
    public static String emptyToNull(String string) {
        return !string.isEmpty() ? string : null;
    }
    
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.isEmpty();
    }
    
    public static String 	nullToEmpty(String string) {
        return string != null ? string : "";
    }
}
