package ch05;

import java.util.concurrent.TimeUnit;

public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        WaitNotifyDemo2 demo2 = new WaitNotifyDemo2();

        new Thread(demo2::m1).start();

        TimeUnit.SECONDS.sleep(1);
        //如果notify先发生，wait方法是收不到的
       demo2.m2();

       Thread thread;


    }
}
