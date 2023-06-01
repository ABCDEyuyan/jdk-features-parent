package ch05;

import java.util.concurrent.TimeUnit;

/**
 * 超时等待：见{@link #wait(long)}方法上的注释
 * 调用任何一个wait方法后，线程就会进入到wait set中，
 * 调用wait方法后会释放锁并不再被调度程序调度以得到执行的机会，除非
 * <ol>
 *     <li>其它线程调用了notify，并且恰好唤醒了本wait线程</li>
 *     <li>其它线程调用了notifyAll</li>
 *     <li>其它线程interrupt本wait线程</li>
 *     <li>等待的超时时间到了</li>
 *     <li>虚假唤醒( spurious wakeup)，详细情况见{@link PauseResumeDemo3}</li>
 * </ol>
 */
public class Main3 {
    public static void main(String[] args) throws Exception {
        //basic();
        basic2();
    }

    private static void basic() {
        WaitNotifyDemo3 demo3 = new WaitNotifyDemo3();
        //等待时间到了之后，wait线程仍然是要去竞争锁的，竞争到了才能继续执行，否则也不能执行的
        demo3.m1();
        System.out.println("======");
    }

    private static void basic2() throws InterruptedException {
        WaitNotifyDemo3 demo3 = new WaitNotifyDemo3();
        //等待时间到了之后，wait线程仍然是要去竞争锁的，竞争到了才能继续执行，否则也不能执行的
        Thread t = new Thread(demo3::m1);
        t.start();
        //主线程休眠一秒，让t线程先执行
        TimeUnit.SECONDS.sleep(1);
        //上面的线程启动后，会wait 3秒钟，此时主线程就在执行m2,并且m2没有执行完毕，没有释放锁
        //那么m1获取不到锁,所以wait之后的代码也不会得到执行，只有m2执行完毕释放锁之后，m1才能继续执行
        demo3.m2();

    }
}
