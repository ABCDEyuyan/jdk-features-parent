package util;

import java.util.concurrent.TimeUnit;

public class ThreadUtils {
    public static void sleep(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void printThreadName() {
        System.out.println("当前线程: " + Thread.currentThread().getName());
    }
}
