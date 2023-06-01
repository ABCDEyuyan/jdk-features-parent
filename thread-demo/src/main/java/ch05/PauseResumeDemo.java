package ch05;

import java.util.concurrent.TimeUnit;

public class PauseResumeDemo {
    private Object lock = new Object();
    private volatile boolean pause = false;

    public void doSth() {
        synchronized (lock) {
            while (true) {
                if (pause == true) {
                    try {
                        //卡住并释放了锁
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                //这个休眠的代码是多余的，就是想让其输出慢一点
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(" do ------");
            }

        }
    }

    public void pause() {
        pause = true;
        System.out.println("after pause--");
    }

    public void resume() {
        synchronized (lock) {
            pause = false;
            //  lock.notify();
            lock.notifyAll();
        }
    }


    public static void main(String[] args) {
        PauseResumeDemo demo = new PauseResumeDemo();

        Thread t = new Thread(demo::doSth);
        t.start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        demo.pause();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //wait方法是会释放锁的，notify和notifyAll是不会释放锁的，只有同步代码执行完毕才会释放锁
        demo.resume();

    }
}
