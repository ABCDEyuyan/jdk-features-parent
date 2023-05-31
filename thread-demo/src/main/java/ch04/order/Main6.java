package ch04.order;

import java.time.LocalTime;

/**
 * 有序性问题：https://blog.csdn.net/jiang_wang01/article/details/113066665
 * 反复运行下面的代码（但不一定能出现flag=true，value是33的情况,效果很难演示出来)
 */
public class Main6 {
    static int value = 33;
    private static boolean flag = false;

    public static void main(String[] args) throws Exception {

        Thread t1 = new ChangeThread();
        Thread t2 = new DisplayThread();
        t1.start();
        t2.start();
    }

    static class ChangeThread extends Thread {
        @Override
        public void run() {
            while (true) {
                System.out.println(Thread.currentThread().getName() + "--- change value to 1024,time:" + LocalTime.now());
                value = 1024;
                System.out.println(Thread.currentThread().getName() + "--- change flag to true,time:" + LocalTime.now());
                flag = true;
                System.out.println(Thread.currentThread().getName() + "--- change end,time:" + LocalTime.now());

            }
        }
    }

    static class DisplayThread extends Thread {
        @Override
        public void run() {
            while (true) {
                if (flag) {
                    System.out.println(Thread.currentThread().getName() + "--- flag is [[true]],value的值是:" + value + ",time:" + LocalTime.now());
                } else {
                    System.out.println(Thread.currentThread().getName() + "--- flag is [[false]],value的值是:" + value + ",time:" + LocalTime.now());
                }
            }
        }
    }
}

