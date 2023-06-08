package ch08;

import java.util.concurrent.Semaphore;

import static util.ThreadUtils.printThreadName;
import static util.ThreadUtils.sleep;

/**
 * Semaphore:信号量的意思，主要用来同时有多少个线程可以访问资源,
 * 比如new Semaphore(3),相当于有3个通行证，最多只有3个线程能同时访问资源。
 * 类似于去停车场停车，剩余车位就是通行证，有剩余车位，你就可以进去停车，没有就等待
 * <h3>基本API</h3>
 * <ul>
 *     <li>{@link Semaphore#acquire()}:获取一个通行证，获取不到就park当前线程</li>
 *     <li>{@link Semaphore#release()}:释放一个通行证，会在park的线程里选择一个线程unPark，
 *     调用release方法时并不要求当前线程通过调用acquire方法获得了许可，这点与notify,notifyAll是不一样的</li>
 * </ul>
 * <h3>其它特性</h3>
 * <p>{@link Semaphore}也支持公平与非公平的特性，通过构造函数指定公平性，默认是非公平的</p>
 * <p>其也支持一次性获取多个许可与释放多个许可的特性，{@link Semaphore#tryAcquire()}支持非阻塞的获取通行证,
 * 其重载方法也支持超时机制</p>
 */
public class SemaphoreDemo {
    // 任意时刻仅允许最多3个线程获取许可:
    final Semaphore semaphore = new Semaphore(3);

    public void access()  {
        // 如果超过了许可数量,其他线程将在此等待:
        try {
            semaphore.acquire();
            printThreadName();
            sleep(2);
            System.out.println("done---");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            semaphore.release();
        }

    }

    public static void main(String[] args) throws Exception{
        //demo();
        demo1();
    }

    private static void demo() {
        SemaphoreDemo demo = new SemaphoreDemo();
        for (int i = 0; i < 5; i++) {
            new Thread(demo::access).start();
        }
    }

    private static void demo1() throws Exception{
        Semaphore semaphore = new Semaphore(3);
        semaphore.acquire();
        //semaphore.release();
    }
}
