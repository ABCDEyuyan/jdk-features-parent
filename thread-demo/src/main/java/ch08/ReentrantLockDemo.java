package ch08;

/**
 * 一个可重入的排它锁实现，与同步(synchronized)代码有着基本相同的含义与行为.
 * 此类实现了Lock接口，此接口中lock与tryLock方法是用来获取锁的，unlock是用来释放锁的
 * <h3>lock与tryLock</h3>
 * <p>lock方法调用时，如果没有其它线程占据资源，那么可以立即获得锁，如果就是本线程占据了锁，那么锁持有数量+1，
 * 如果别的线程占据了锁,本线程获取不到锁，就会调用LockSupport的park方法暂停本线程的执行(current thread becomes disabled
 * for thread scheduling purposes and lies dormant until the lock has been acquired)</p>
 * <h3>推荐用法</h3>
 * 在调用了lock之后紧跟一个try代码块，在finally里调用unlock，见
 *  <pre> {@code
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
}
