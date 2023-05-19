package ch04;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyTask runnable = new MyTask();
        Thread t = new Thread(runnable);
        t.start();

        TimeUnit.SECONDS.sleep(3);
        runnable.stop=true;
        System.out.println("done");

    }
}
