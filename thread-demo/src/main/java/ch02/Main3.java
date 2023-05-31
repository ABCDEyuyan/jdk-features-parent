package ch02;

import java.util.concurrent.TimeUnit;

public class Main3 {
    public static void main(String[] args) throws InterruptedException {

        //interruptAndSleep();

        interruptWithoutSleep();

    }

    private static void interruptWithoutSleep() throws InterruptedException {
        Runnable runnable = () -> {
            int i = 0;
            while (i < 2500) {
                System.out.println("开始 isInterrupted()实例方法:" + Thread.currentThread().isInterrupted());

                System.out.println("-----");
                i++;
                //interrupted是判断是否被打断过
                //静态方法interrupted（）除了判断当前打断标志位以外，还会清掉标志位
                if (Thread.interrupted()) {
                    System.out.println("isInterrupted()实例方法:" + Thread.currentThread().isInterrupted());
                    break;
                }

            }
        };

        Thread t = new Thread(runnable);
        t.start();

        TimeUnit.MILLISECONDS.sleep(10);

        t.interrupt();
    }

    private static void interruptAndSleep() throws InterruptedException {
        Runnable runnable = () -> {
            int i = 0;
            while (i < 1000) {
                System.out.println("开始 isInterrupted()实例方法:" + Thread.currentThread().isInterrupted());
                System.out.println("-----");
                i++;
                try {
                    //休眠这种可以抛出InterruptedException的方法，会检查中断(打断)标志位
                    //如果检测到了的话，就抛出异常
                    //当sleep方法检测到打断标志位是“true”，sleep干了2个事情
                    //1. 抛出异常，2，清理打断标志位，设置为false
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    //e.printStackTrace();
                    System.out.println("线程被打断");

                    //isInterrupted()方法只是读取打断标志位的值，不会改变这个标志位的值
                    System.out.println("isInterrupted()实例方法:" + Thread.currentThread().isInterrupted());
                    //自己写代码处理打断逻辑，你也可以不管它
                    break; //结束循环，也就意味着线程执行即将结束
                }
            }

        };

        Thread t = new Thread(runnable);
        t.start();

        TimeUnit.SECONDS.sleep(3);
        //让t代表的线程打断（interrupt）其执行
        //让线程设置一个标志位：线程被打断
        t.interrupt();
    }
}
