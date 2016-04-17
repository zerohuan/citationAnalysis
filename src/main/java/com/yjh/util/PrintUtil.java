package com.yjh.util;

import java.lang.reflect.Field;

/**
 * print tools
 *
 * Created by yjh on 15-10-10.
 */
public class PrintUtil {
    public static String toHex(int num) {
        return String.format("%x", num);
    }

    public static String toHex(byte[] bytes) {
        StringBuilder stringBuilder = new StringBuilder();
        for(byte b : bytes) {
            stringBuilder.append(String.format("%02x", b)).append(" ");
        }
        return stringBuilder.toString();
    }

    public static void printAllClassName(Class<?>...clazs) {
        for(Class<?> c : clazs) {
            System.out.println(c.getName() + " " + c.getCanonicalName());
        }
    }

    public static void printAllMethod(Field...fields) {
        for(Field f : fields) {
            System.out.println(f.getName());
        }
    }
}
