package ch04;

/**
 *
 */
public class ThreadLocalDemo {
    //只是实例化了一个ThreadLocal的对象，里面还是没有存放数据
    static ThreadLocal<Integer> local = new ThreadLocal<>();
    static ThreadLocal<Integer> local2 = ThreadLocal.withInitial(() -> 0);

    public static void main(String[] args) throws InterruptedException {
        //basicUsage();

        commanUsage();

        //commanUsageWithInitValue();
    }

    private static void commanUsageWithInitValue() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Integer integer = local2.get();
                integer++;
                local2.set(integer);
            }
            System.out.println(Thread.currentThread().getName() + "  --  " + local2.get());
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 2000; i++) {
                Integer integer = local2.get();
                integer++;
                local2.set(integer);
            }
            System.out.println(Thread.currentThread().getName() + "  --  " + local2.get());
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //输出null，因为在主线程没有操作它，没有设置过值
        System.out.println(Thread.currentThread().getName() + "  --  " + local2.get());
    }

    private static void commanUsage() throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) {
                Integer integer = local.get();
                if (integer == null) {
                    integer = 0;
                }
                integer++;
                local.set(integer);
            }
            System.out.println(Thread.currentThread().getName() + "  --  " + local.get());
        }, "t1");

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 2000; i++) {
                Integer integer = local.get();
                if (integer == null) {
                    integer = 0;
                }
                integer++;
                local.set(integer);
            }
            System.out.println(Thread.currentThread().getName() + "  --  " + local.get());
        }, "t2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        //输出null，因为在主线程没有操作它。没有放过值
        System.out.println(Thread.currentThread().getName() + "  --  " + local.get());
    }

    private static void basicUsage() {
        Integer integer = local.get();
        if (integer == null) {
            System.out.println("kong");
            integer = 100;
        }
        local.set(integer);
        System.out.println(local.get());
    }

    //假定有多个线程都执行这个方法，那么xx由于是局部变量
    //每个线程都有自己的一个xx的副本，互不干扰，所以没有线程不安全的问题

    //方法的参数与局部变量都是线程私有的，不存在安全性的问题
    public void x(int a, int b) {
        int xx = 100;
    }

}

