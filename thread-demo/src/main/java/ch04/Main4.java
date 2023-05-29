package ch04;

/**
 * 原子性问题:演示锁对象不一致的问题
 */
public class Main4 {
    public static void main(String[] args) throws InterruptedException {
        MyCounter3 counter = new MyCounter3();

        Runnable r1 = ()->{
            for (int i = 0; i < 1000; i++) {
                counter.incr();
            }
        };

        Runnable r2 = ()->{
            for (int i = 0; i < 1000; i++) {
                counter.decre();
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
