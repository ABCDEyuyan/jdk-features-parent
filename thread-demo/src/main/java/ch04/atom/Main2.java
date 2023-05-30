package ch04.atom;

/**
 * 《Java并发编程实战》书中给出定义：
 * 当多个线程访问某个类时，不管运行时环境采用何种调度方式或者这些线程将如何交替执行，
 * 并且在调用代码中不需要任何额外的同步，这个类都能表现出正确的行为，那么这个类就是线程安全的。
 *
 * 多线程环境下三大因素：原子性、有序性与可见性
 * 演示原子性的问题：用同步方法解决
 */
public class Main2 {
    public static void main(String[] args) throws InterruptedException {
        //singleThreadDemo(); 一定要是同一个对象，不然锁对象不一样就不会有同步效果
        multiThreadDemo();


    }

    private static void multiThreadDemo() throws InterruptedException {
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
