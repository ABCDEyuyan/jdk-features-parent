package ch05;

import java.util.concurrent.TimeUnit;

/**
 * 对比PauseResumeDemo案例，用while替换了if
 * 虚假唤醒的解释见{@link #doSth()}与{@link #wait(long)}方法上的注释
 */
public class PauseResumeDemo3 {
    private Object lock = new Object();
    private volatile boolean pause = false;

    /**
     * 这是学校教材上的写法，有bug，没有考虑虚假唤醒的问题<br/>
     *
     * <h3>虚假唤醒（spurious wakeiups)</h3>
     * <p>定义：在线程等待唤醒的过程中，等待的线程被唤醒后，
     * 在条件不满足的情况下依然继续向下执行代码.<br/>
     * 虚假唤醒这种情况不仅仅是java语音独有的问题，其它编程语言也都有这种情况。
     * 是os底层的问题，在java中，即使没有调用过notify或notifyAll，都有可能偶尔被唤醒
     * </p>
     * <h3>虚假唤醒出现的情况</h3>
     * 下面是一个便于理解的说法，并不正确。
     * <ol>
     *     <li>假定A线程因为某个条件不满足进入到了wait状态</li>
     *     <li>其它线程调用了notify或者notifyAll</li>
     *     <li>第一步处于阻塞状态的线程被唤醒，接着wait方法之后的代码运行，
     *     但此时条件还是处于不满足的情况，这种情况就不应该被唤醒，只有条件满足后才应该被唤醒</li>
     * </ol>
     * <h3>解决办法</h3>
     * 通常是用while循环而不是用if来对条件进行判断，<b>见wait方法的源码注释</b>
     * <pre class="code">
     *  synchronized(obj){
     *      while(condition does not hold(条件不满足)){
     *          obj.wait();
     *      }
     *  }
     * </pre>
     *<h3>虚假唤醒参考资料</h3>
     * https://blog.csdn.net/hgmolk/article/details/109855802
     * https://www.zhihu.com/question/271521213
     * https://en.m.wikipedia.org/wiki/Spurious_wakeup
     * https://jenkov.com/tutorials/java-concurrency/thread-signaling.html
     */
    public void doSth() {
        synchronized (lock) {
            while (true) {
                //为了防止虚假唤醒，就需要循环判断条件是否满足执行要求
                while (pause == true) {
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
        PauseResumeDemo3 demo = new PauseResumeDemo3();

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
