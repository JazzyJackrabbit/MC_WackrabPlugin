package com.jackrabbit.wackrab.utils;

public class Log {
	public static void log(String txt) {
        System.out.println(txt);
    }
	public static void log(Object txt) {
        System.out.println(txt.toString());
    }
}
