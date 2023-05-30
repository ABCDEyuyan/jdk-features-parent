package ch04.atom;

/**
 * 原子性问题：用同步代码块解决
 */
public class Main3 {
    public static void main(String[] args) throws InterruptedException {
        multiThreadDemo();
    }

    private static void multiThreadDemo() throws InterruptedException {
        MyCounter2 counter = new MyCounter2();

        Runnable r1 = ()->{
            for (int i = 0; i < 1000; i++) {
                counter.incr();
            }
        };

        Runnable r2 = ()->{
            for (int i = 0; i < 1000; i++) {
                counter.incr();
            }
        };
        Thread t1 = new Thread(r1, "t1");
        Thread t2 = new Thread(r2, "t2");

        t1.start();
        t2.start();


        t1.join();
        t2.join();

        System.out.println("count:" + counter.getCount());
    }

}
