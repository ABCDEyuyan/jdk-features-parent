package ch02;

public class Main4 {
    public static void main(String[] args) {
        //yield(让出）

        Runnable r = ()->{
            //分配到的时间片，让出，
            System.out.println("before-----");
            //给线程调度程序一个提示，表明我让出分配到的时间片，但调度程序可以忽略
            //主要用在调试与测试目的，一般生产代码很少出现让出执行机会的情况。
            Thread.yield();
            System.out.println("after-----");//输出
        };

        new Thread(r).start();
    }
}
