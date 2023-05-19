package ch02;

import java.util.concurrent.TimeUnit;

public class Main2 {
    public static void main(String[] args) {
        Runnable runnable = ()->{
            int i = 0;
            while (i < 1000) {
                System.out.println("-----");
                i++;
                try {
                    //休眠1秒是个大概值，不要认为是绝对的1秒钟
                   // Thread.sleep(1000);//以毫秒为单位
                    // 另外的写法
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };

        Thread t = new Thread(runnable);
        t.start();

    }
}
