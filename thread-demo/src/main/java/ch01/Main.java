package ch01;

public class Main {
    public static void main(String[] args) {
        //进程 与 线程
        //下面的代码只是创建了一个类的对象，还没有创建出线程

        System.out.println(Thread.currentThread().getName());
        FirstThread firstThread = new FirstThread();
        //调用start之后才真正创建了一个线程。
        firstThread.start();

        aaaa();


    }
//线程的执行是交错的，无法预知
    public static void aaaa() {
        System.out.println(Thread.currentThread().getName());
        int max = 1000000;
        int i = 0;
        while (i < max) {
            System.out.println("111111 main");
            i++;
        }
    }
}
