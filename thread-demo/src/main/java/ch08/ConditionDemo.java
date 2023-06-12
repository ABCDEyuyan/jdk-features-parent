package ch08;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Condition主要功能是实现了Object类的wait,notify之类的功能，每一个{@link Lock}接口实现都可以通过方法
 * {@link Lock#newCondition()}方法可以获取Condition对象。
 * <p>Condition提供的await()、signal()、signalAll()方法的含义和synchronized锁对象的wait()、notify()、notifyAll()是一致的,
 * Condition提供的方法都需要在获取锁的情况下才能使用，否则会抛出IllegalMonitorStateException异常</p>
 *<ul>
 *     <li>await()会释放当前锁，进入等待状态</li>
 *     <li>signal()会唤醒某个等待线程</li>
 *     <li>signalAll()会唤醒所有等待线程</li>
 *     <li>唤醒线程从await()返回后需要重新获得锁</li>
 *</ul>
 * @see Condition
 * @see Lock
 */
public class ConditionDemo {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private Queue<String> queue = new LinkedList<>();

    public void test() {
       // condition.signal();
        condition.signalAll();
       /* try {
            condition.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
    }
    public void addTask(String s) {
        lock.lock();
        try {
            queue.add(s);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public String getTask() {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                condition.await();
            }
            return queue.remove();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        } finally {
            lock.unlock();
        }
    }

    public static void main(String[] args) {
        ConditionDemo demo = new ConditionDemo();
        demo.test();
    }
}
