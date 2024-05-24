package src.utils;

import src.enums.LoggingType;

public class LoggingUtil {
    public static final String ANSI_WHITE = "\u001B[97m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String[] prefixes = {
        ANSI_WHITE  +   "[LOG] ",
        ANSI_RED    +   "[ERROR] "
    };

    public static void logln(Object obj, LoggingType type){
        switch (type) {
            case DEBUG:
                System.out.println(prefixes[0] + obj);
                break;
            case ERROR:
                System.out.println(prefixes[1] + obj);
                break;
        }
    }
}
