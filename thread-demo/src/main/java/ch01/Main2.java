package ch01;

public class Main2 {
    public static void main(String[] args) {
        Runnable runnable = () -> System.out.println("这是线程的代码" + Thread.currentThread().getName());
        //runnable.run();//直接调用就是让其运行在主线程

        Thread t = new Thread(runnable, "myThread");
        t.start();
        //下面的代码即使你运行程序100亿次都是在线程的前面，也不能证明它一定是在前面
        System.out.println("xxxx");

    }
}
