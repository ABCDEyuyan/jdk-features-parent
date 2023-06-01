package ch05;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 利用LockSupport实现暂停恢复的操作
 */
public class PauseResumeDemo2 {
    private Object lock = new Object();
    private volatile boolean pause = false;

    /**
     * doSth与resume方法不必是同步方法，LockSupport.park不会有释放monitor锁的
     */
    public void doSth() {

        while (true) {
            System.out.println(" do ------");
            if (pause) {
                LockSupport.park();
            }
        }
    }

    public void pause() {
        pause = true;
    }

    public void resume(Thread thread) {
        pause = false;
        LockSupport.unpark(thread);
    }


    public static void main(String[] args) throws InterruptedException {
        PauseResumeDemo2 demo = new PauseResumeDemo2();
        Thread t = new Thread(demo::doSth);
        t.start();
        //模拟一段时间后暂停任务
        TimeUnit.SECONDS.sleep(3);
        demo.pause();
        System.out.println("暂停了-----------");
        // 模拟暂停了一段时间之后又恢复执行
        TimeUnit.SECONDS.sleep(3);
        demo.resume(t);
        System.out.println("恢复了-----------");
    }
}
