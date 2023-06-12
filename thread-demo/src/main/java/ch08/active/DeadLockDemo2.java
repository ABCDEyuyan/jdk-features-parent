package ch08.active;

public class DeadLockDemo2 {
    
    private Object lock = new Object();
    public synchronized void a(){
        System.out.println("a---");
        c();
    }

    public synchronized void b(){
        System.out.println("b--");
    }

    public  void c(){
        synchronized (lock) {
            System.out.println("c--");
        }
    }

    public  void d(){
        synchronized (lock) {
            System.out.println("d--");
            b();
        }
    }

    public static void main(String[] args) {
        //下面写法出现死锁的几率很高
        DeadLockDemo2 deadLock = new DeadLockDemo2();
        Thread t1 = new Thread(deadLock::a);
        Thread t2 = new Thread(deadLock::d);
        t1.start();
        //下面的代码取消注释，就不会有死锁
        //TimeUnit.SECONDS.sleep(1);
        t2.start();
    }
}
