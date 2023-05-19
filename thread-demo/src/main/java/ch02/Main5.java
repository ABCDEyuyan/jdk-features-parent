package ch02;

public class Main5 {
    public static void main(String[] args) {
        Runnable r = () -> {

            while (true) {

            }
        };

        Thread t = new Thread(r);
        //设置为true，表明是一个守护线程或者叫后台线程

        //后台线程对应的线程叫前台线程，main线程就归属于前台线程
        //程序退出的前提：没有一个前台线程在执行，虚拟机就会退出
        //setDaemon这个方法必须在启动（start）之前设置
        t.setDaemon(true);
        t.start();
        System.out.println("done---");
    }
}
