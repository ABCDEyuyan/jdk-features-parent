package ch07;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 链表实现的一个有界阻塞队列，队尾（tail）入队，对头（head）出队
 * <h3>入队操作</h3>
 * <ul>
 *     <li>{@link java.util.concurrent.BlockingQueue#add(Object)}:非阻塞，内部调用offer(Object)方法，队列满了，直接抛异常</li>
 *     <li>{@link java.util.concurrent.BlockingQueue#offer(Object)}:非阻塞，满了直接返回false，否则立即返回true，有界队列推荐用offer而不要用add</li>
 *     <li>{@link java.util.concurrent.BlockingQueue#put(Object)}:阻塞，队列满了一直阻塞，直到队列不满或被中断</li>
 *     <li>{@link java.util.concurrent.BlockingQueue#offer(Object, long, TimeUnit)}:阻塞，与put类似，只不过多了等待超时机制</li>
 * </ul>
 * <h3>出队操作</h3>
 * <ul>
 *     <li>{@link BlockingQueue#poll()}:非阻塞，没有元素，直接返回null，有元素就出队</li>
 *     <li>{@link BlockingQueue#remove()}:非阻塞，删除队列头元素，没有元素返回false</li>
 *     <li>{@link BlockingQueue#take()}:阻塞，队列空了就一直阻塞，直到队列不为空或被中断</li>
 *     <li>{@link java.util.concurrent.BlockingQueue#poll(long, TimeUnit)}:队列不为空，直接出队;队列为空且超时，返回null;队列为空未超时,阻塞等待</li>
 * </ul>
 * <h3>主要特性</h3>
 * <ul>
 *     <li>通过构造函数指定边界（bound），不指定就是整数最大值</li>
 *     <li>实现了 Optional操作</li>
 *     <li>通常会比{@link java.util.concurrent.ArrayBlockingQueue}有更高的吞吐量</li>
 *     <li>比{@link java.util.concurrent.ArrayBlockingQueue}在大多数并发应用中有更低的可预测性的性能</li>
 *     <li>内部有两把锁，入队操作与出队操作各一把，互不影响，两个Condition来控制</li>
 * </ul>
 * <h3>参考资料</h3>
 * <ul>
 *     <li><a href="https://blog.csdn.net/qq_40922616/article/details/116304863">各种BlockingQueue的介绍</a></li>
 * </ul>
 * @see java.util.concurrent.ArrayBlockingQueue
 * @see java.util.concurrent.PriorityBlockingQueue
 * @see java.util.concurrent.DelayQueue
 * @see java.util.concurrent.SynchronousQueue
 * @see java.util.concurrent.TransferQueue
 */
public class LinkedBlockingQueueDemo {
}
