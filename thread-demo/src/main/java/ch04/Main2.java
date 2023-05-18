package ch04;

/**
 * 演示原子性的问题
 */
public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        //singleThreadDemo(); 一定要是同一个对象，不然锁对象不一样就不会有同步效果
        MyCounter counter = new MyCounter();


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
        //最终的结果应该是0，正数，负数都有可能
        //因为2个线程交替执行，并且i++与i--不是原子操作
        //解决这个问题，可以用支持原子操作的类代替int类型或者用synchronize
        System.out.println("count:" + counter.getCount());


    }

    private static void singleThreadDemo() {
        MyCounter counter = new MyCounter();

        for (int i = 0; i < 1000; i++) {
            counter.incr();
        }
        System.out.println("count:" + counter.getCount());

        for (int i = 0; i < 1000; i++) {
            counter.decre();
        }
        System.out.println("count:" + counter.getCount());
    }
}
