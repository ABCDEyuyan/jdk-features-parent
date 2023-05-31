package ch05;

/**
 * 1.wait notify notifyAll来自于Object类，所以所有的对象都有这三个方法
 * 2.它们的调用必须是在同步代码内
 */
public class WaitNotifyDemo {
    private Object lock = new Object();//16字节

    public synchronized void m1() {
        //
        try {
            System.out.println("before ---");
            //wait之后表示卡住，后面的代码不执行，wait所在的方法m1还没有执行完毕
            this.wait();
            System.out.println("after ----");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void m2() {
        this.notify();
    }

    public void m3() {
        synchronized (lock) {
            try {
                System.out.println("before ---");
                //不能调用this.wait
                //原因是因为这3个方法能调用的前提是：必须先获得锁
                //this.wait();
                lock.wait();
                System.out.println("after ----");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void m4() {
        synchronized (lock) {
            // this.notify(); //错误
            lock.notify();
        }
    }
}
