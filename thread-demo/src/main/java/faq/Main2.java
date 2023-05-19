package faq;

public class Main2 {
    public static void main(String[] args) {
        Runnable runnable = ()->System
                .out
                .println(Thread.currentThread().getName());


        Thread t = new Thread(runnable);
        t.start();//只有调用start才表示创建并启动了一个线程
        t.run(); //runnable.run();没有启动一个线程
    }
}
