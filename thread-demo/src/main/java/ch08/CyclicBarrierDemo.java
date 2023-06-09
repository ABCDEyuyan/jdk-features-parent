package ch08;
import java.util.concurrent.CyclicBarrier;

import static util.ThreadUtils.sleep;
import static util.ThreadUtils.threadName;

/**
 * Cyclic:循环的意思，Barrier:屏障，栅栏的意思，所以{@link CyclicBarrier}翻译为循环屏障，
 * 是一个可以反复使用的屏障。
 * <p>其作用类似于一起去旅游，到了一个地方后，先要清点人数，所有先到的人要等其它人到了之后才开始游玩，
 * 游玩结束之后去到另一个景点，也是先清点人数后才开始游玩，周而复始</p>
 * <h3>基本API</h3>
 * <ul>
 *     <li>{@link CyclicBarrier#CyclicBarrier(int, Runnable)}:第一个参数是参与（parties）线程的个数，
 *     这些个数的线程都到达屏障后才能继续执行，第二个参数是最后一个到达线程要做的任务，相当于所有线程到达屏障后要干的事情</li>
 *     <li>{@link CyclicBarrier#await()}:表示自己已经到达屏障，会让自己进入等待状态（LockSupport.park）</li>
 * </ul>
 */
public class CyclicBarrierDemo {

    public static void main(String[] args) {
        int num = 5;
        Runnable r = ()->{
            System.out.println(threadName() + " 所有线程到达屏障，在最后一个到达屏障的线程里完成最后的任务---");
        };
        //相当于5个线程都达到屏障之后才能继续执行
        CyclicBarrier barrier = new CyclicBarrier(5,r);

        for (int i = 0; i < num; i++) {
            MyTask task = new MyTask(barrier);
            new Thread(task,"thread " + i).start();
        }
    }

    static class MyTask implements Runnable{
        private CyclicBarrier barrier;

        public MyTask(CyclicBarrier barrier) {
            this.barrier = barrier;
        }

        @Override
        public void run() {
            try {
                sleep(1);//模拟做了一些事情
                System.out.println(threadName() + " 到达屏障 A---");
                barrier.await();
                System.out.println(threadName() + " 穿过屏障 A---");

                sleep(1);//模拟做了一些事情
                System.out.println(threadName() + " 到达屏障 B---");
                barrier.await();
                System.out.println(threadName() + " 穿过屏障 B***");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
