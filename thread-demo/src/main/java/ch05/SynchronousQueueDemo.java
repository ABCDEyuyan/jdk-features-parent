package ch05;

import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.TimeUnit;

/**
 * {@link SynchronousQueue}在{@link Executors#newCachedThreadPool()}方法中有使用到，
 * 它是一个阻塞队列，也称为同步队列或配对队列，它的每一个"插入"操作都必须等待其它线程上的"移除"操作，反之亦然。
 * <p>类似于小朋友配对玩游戏的场景，一个小孩必须找到一个和他搭档的伙伴，它们两才能开始游戏</p>
 * <h3>基本特性</h3>
 * <ul>
 *     <li>可以理解为容量为0的队列</li>
 *     <li>不能peek,总返回null，因为当你删除的时候，元素才会出现</li>
 *     <li>不允许null元素</li>
 *     <li>不能迭代（iterate)</li>
 *     <li>此队列支持公平性，就是让生产者与消费者遵循FIFO原则</li>
 * </ul>
 * <h3>线程池使用SynchronousQueue的理由</h3>
 * <p>由于ThreadPoolExecutor内部实现任务提交的时候调用的是工作队列（BlockingQueue接口的实现类）的非阻塞式入队列方法（offer方法），
 * 因此，在使用SynchronousQueue作为工作队列的前提下，客户端代码向线程池提交任务时，
 * 而线程池中又没有空闲的线程能够从SynchronousQueue队列实例中取一个任务，那么相应的offer方法调用就会失败（即任务没有被存入工作队列）。
 * 此时，ThreadPoolExecutor会新建一个新的工作者线程用于对这个入队列失败的任务进行处理（假设此时线程池的大小还未达到其最大线程池大小）.<br/>
 * 所以，可以简单理解为根本没有用到队列来存储任务，有空闲线程就利用空闲执行任务，没有就创建线程，达到最大数量还有新提交任务就进入任务的拒绝策略处理
</p>
 * <h3>参考资料</h3>
 * <ul>
 *     <li><a href="https://juejin.cn/post/7031196740128768037">了解SynchronousQueue</a></li>
 * </ul>
 * @see java.util.concurrent.ThreadPoolExecutor
 */
public class SynchronousQueueDemo {
    public static void main(String[] args) {
        SynchronousQueue<String> synchronousQueue = new SynchronousQueue<>();
        new Thread(() -> {
            try {
                System.out.println(Thread.currentThread().getName() + "\t 入队列 1");
                synchronousQueue.put("1");
                System.out.println(Thread.currentThread().getName() + "\t 入队列 2");
                synchronousQueue.put("2");
                System.out.println(Thread.currentThread().getName() + "\t 入队列 3");
                synchronousQueue.put("3");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "AAAAA").start();

        new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "\t 出队列 " + synchronousQueue.take());
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "\t 出队列 " + synchronousQueue.take());
                TimeUnit.SECONDS.sleep(5);
                System.out.println(Thread.currentThread().getName() + "\t 出队列 " + synchronousQueue.take());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }, "BBBBB").start();


    }
}
