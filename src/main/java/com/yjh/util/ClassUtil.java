package com.yjh.util;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

/**
 *
 * Created by yjh on 15-12-9.
 */
public class ClassUtil {
    /**
     * 打印一个对象的实例数据状态
     * @param obj
     * @param out
     */
    public static void printStatus(Object obj, PrintStream out){
        Class<?> claz = obj.getClass();
        Field[] fields = claz.getDeclaredFields();
        try {
            for(Field field : fields) {
                field.setAccessible(true);
                out.print(field);
                out.print("\t");
                Object fieldObj = field.get(obj);

                if(fieldObj == null)
                    out.println("null");
                else {
                    Class<?> fieldClass = fieldObj.getClass();
                    if(fieldClass.isArray()) {
                        printArray(fieldObj, out);
                        out.println();
                    } else {
                        out.println(fieldObj);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 反射打印数组，如果是多维数组递归打印
     * @param arr
     * @param out
     */
    public static void printArray(Object arr, PrintStream out) {
        Class<?> arrClass = arr.getClass();
        if(arrClass.isArray()) {
            int length = Array.getLength(arr);
            out.print('[');
            for(int i = 0; i < length; ++i) {
                Object o = Array.get(arr, i);
                if(o.getClass().isArray()) {
                    printArray(o, out);
                } else {
                    out.print(o);
                    if(i < length - 1)
                        out.print(",");
                }
            }
            out.print(']');
        }
    }

    public static void main(String[] args) {
        String s = "132";
        char[][] chars = {{'1','2','3'}};
        printStatus(s, System.out);
//        printArray(chars, System.out);
    }
}
