package com.util;

import java.util.concurrent.ExecutorService;

/**
 * 我的并发辅助工具类
 *
 * Created by yjh on 15-12-28.
 */
public final class ThreadUtils {
    //查看线程或者Executor的状态
    public static void printStatus(Object o) {
        if (o instanceof Thread) {
            Thread t = (Thread)o;
            System.out.println("Thread " + t.getName() + " " + t.getState());
        }
        else if (o instanceof ExecutorService) {
            ExecutorService e = (ExecutorService)o;
            System.out.println("Executor " + e.isShutdown() + " " + e.isTerminated());
        }
    }
}
