package ch08;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个可重入的排它锁实现，与同步(synchronized)代码有着基本相同的含义与行为.
 * 此类实现了Lock接口，此接口中lock与tryLock方法是用来获取锁的，unlock是用来释放锁的
 *
 * <h3>lock与tryLock</h3>
 * <p>lock方法调用时，如果没有其它线程占据资源，那么可以立即获得锁，如果就是本线程占据了锁，那么锁持有数量+1，
 * 如果别的线程占据了锁,本线程获取不到锁，就会调用LockSupport的park方法暂停本线程的执行(current thread becomes disabled
 * for thread scheduling purposes and lies dormant until the lock has been acquired)</p>
 * <h3>公平性</h3>
 * <p>通过在实例化的时候在构造函数里指定true参数来创建公平锁，否则就是非公平锁实现。
 * 在锁竞争的情况下，公平锁倾向于把锁给最长等待时间的线程，在很多线程访问的情况下，公平锁机制会显著降低吞吐量，
 * 但公平锁的好处是保证不会出现饥饿的产生。</p>
 * <p>需要注意的是tryLock方法并不考虑公平性设置，只要锁能获取到，tryLock立即就会获取到锁，而不管是否有其它线程正在等待获取锁</p>
 *
 * <h3>推荐用法</h3>
 * 在调用了lock之后紧跟一个try代码块，在finally里调用unlock，见
 * <pre> {@code
 * class X {
 *   private final ReentrantLock lock = new ReentrantLock();
 *   // ...
 *
 *   public void m() {
 *     lock.lock();  // block until condition holds
 *     try {
 *       // ... method body
 *     } finally {
 *       lock.unlock()
 *     }
 *   }
 * }}</pre>
 */
public class ReentrantLockDemo {
    private final Lock lock = new ReentrantLock();
    private int count;

    public void add() {
        lock.lock();
        try {
            for (int i = 0; i < 10000; i++) {
                count += 1;
            }

        } finally {
            lock.unlock();
        }
    }

    public void add2() {
        for (int i = 0; i < 10000; i++) {
            count += 1;
        }
    }

    public int getCount() {
        return count;
    }

    public static void main(String[] args) throws Exception {
        ReentrantLockDemo demo = new ReentrantLockDemo();
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Thread thread = new Thread(demo::add);
            //Thread thread = new Thread(demo::add2);//会出现100000以外的值
            threads.add(thread);
            thread.start();
        }

        for (Thread thread : threads) {
            thread.join();
        }
        System.out.println("demo.getCount() = " + demo.getCount());
    }
}
