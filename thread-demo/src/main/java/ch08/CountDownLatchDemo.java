package ch08;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * CountDown:倒计时，倒数的意思；Latch:门栓的意思。
 * 此锁的含义类似门上安装了多把锁，只有这些锁都打开了，你才能把门打开，才能出去。
 * 此锁就是让一个或多个线程等待，直到其它线程的任务都执行完毕（可以理解为：锁一个一个打开），
 * 之后这个或这些等待的线程就会被唤醒以继续执行任务。
 * <h3>基本原理</h3>
 * <p>CountDownLatch 是基于计数器的原理实现的，它内部维护了一个整型的计数器。
 * 创建一个 CountDownLatch 对象时，需要指定一个初始计数值，该计数值表示需要等待的线程数量。
 * 每当一个线程完成了其任务，它调用 CountDownLatch 的 countDown() 方法，计数器的值就会减一。
 * 当计数器的值变成 0 时，等待的线程就会被唤醒，继续执行它们的任务</p>
 * <h3>api方法解释</h3>
 * <ul>
 *     <li>{@link CountDownLatch#await()}:让当前线程等待(LockSupport.park)，直到计数到达0或者当前线程被interrupted，如果计数为0的时候调用await就立即返回不等待</li>
 *     <li>{@link CountDownLatch#countDown()}:计数器减一，直到为0(不会出现负数情况，<=0啥都不干),所有等待的线程重新开始进行调度执行</li>
 *     <li>{@link CountDownLatch#getCount()}:返回当前计数器的值</li>
 * </ul>
 * <h3>基本用法</h3>
 * <p>见{@link CountDownLatch}源码中的注释,下面的案例就是其中的一种典型用法</p>
 */
public class CountDownLatchDemo {

    public static void main(String[] args) throws Exception{
        //下面的例子是计算1-60000的和，依据cpu的核心数，把数字拆分成对应的分数，每个cpu对一部分数求和，最后在主线程进行最后的汇总
        int cors = Runtime.getRuntime().availableProcessors();

        ExecutorService pool = Executors.newFixedThreadPool(cors);
        CountDownLatch latch = new CountDownLatch(cors);
        List<MyCalcTask> tasks = new ArrayList<>();
        int total = 0;

        System.out.println("latch.getCount() = " + latch.getCount());
        //这里的算法是类比分页的算法做的，size就是每页记录数量，cors就是总页数，(max-1+1)就是总记录数
        int max = 60000;
        int size = (int)Math.ceil(max *1.0 / cors);
        for (int i = 1; i <= cors; i++) {
            int start = 1+ (i-1)* size;
            int end =Math.min(i*size,max) ;
            MyCalcTask task = new MyCalcTask(start, end, latch);
            tasks.add(task);
            pool.submit(task);
        }

        latch.await();

        for (MyCalcTask task : tasks) {
            total += task.getSum();
        }
        System.out.println("total = " + total);
        pool.shutdown();
    }


    static class MyCalcTask implements Runnable{
        private int start;
        private int end;

        private int sum;
        private CountDownLatch latch;
        public MyCalcTask(int start, int end,CountDownLatch latch) {
            this.start = start;
            this.end = end;
            this.latch = latch;
        }

        @Override
        public void run() {
            for (int i = start; i <= end; i++) {
                sum +=i;
            }
            latch.countDown();
        }

        public int getSum() {
            return sum;
        }
    }
}
