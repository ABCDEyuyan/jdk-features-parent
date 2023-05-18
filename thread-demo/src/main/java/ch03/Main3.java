package ch03;

import java.util.function.IntConsumer;

public class Main3 {
    public static void main(String[] args) {
        //创建这段lambda代码是在main线程中创建的
        IntConsumer consumer = (d)->{
            System.out.println("wo xiao fei " + d);
            System.out.println(Thread.currentThread().getName());
        };

        MyTask3 task3 = new MyTask3(100,consumer);
        task3.setSecond(20);

        Thread t = new Thread(task3);
        t.start();
    }
}
