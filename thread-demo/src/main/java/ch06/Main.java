package ch06;

import java.util.concurrent.*;

/**
 * 讲了
 * 1.线程池创建
 * 2.提交任务
 * 3.关闭线程池
 *
 * 1.线程池创建：
 * 	Executors.newSingle..
 * 	Executors.newCached..
 * 	Executors.newFixed
 *
 * 	ExecutorService
 * 2.线程池关闭：
 * 	shutdown();优雅关闭---等着提交的任务执行完毕
 * 	不再接收新任务.正在执行的任务，利用中断关闭
 * 	还没有开始执行的任务会被执行
 *
 * 	shutdownNow:立即马上关闭，正在执行的任务，利用中断关闭
 * 	还没有开始执行的任务就返回,此方法返回List<Runnable>
 * 3.提交任务：
 * 	1.接受Runable(void run())与Callable(T call())
 *
 * 	2.submit(runnable)
 *
 * 	3.Future<?> future= submit(callable);
 *
 * 	future.get()
 *
 * new ThreadPoolExecutor(2,5,30,second,queue)
 *
 * 1.刚创建出来，此时池里没有线程，除非
 * 调用prestartAllCoreThreads()
 *
 * 2.submit提交任务 ，创建一个线程去执行
 * 3.又提交--由于现在还没有超过核心线程数量设定（2），
 * 又创建一个线程去执行,即使池里现在有空闲线程
 * 4.又提交任务,此时由于core（=2）已经满了，
 * 此时就不再创建线程，直接让任务入队
 * 5.再提交任务，不停入队，直达队列满了,
 * 在这种情况下，即使有空闲线程，也是先入队
 * 6.再提交任务，此时队列也满了，
 * 但还没有超过最大线程数量，
 * 此时就会再创建线程去执行任务
 * 7.再提交任务，线程最大数量也到了，
 * 就进行拒绝策略的处理
 * 8.中间有线程执行任务完毕，线程就空闲了
 * ，如果达到你设定的空闲时间，就销毁此线程，
 * 但是不会销毁core线程，也就是只销毁我们这里设定(5-2)这3个非核心线程
 *
 * <h3>线程池的小结</h3>
 * <ul>
 *     <li>核心线程：创建出来之后就不会被销毁，那个keepAliveTime默认对核心线程无效，
 *  除非调用allowCoreThreadTimeOut(true)</li>
 *  <li>有任务，就创建线程去处理,直到达到核心线程的数量</li>
 *  <li>核心线程数量够了，就放到队列中</li>
 *  <li>队列满了，就额外创建非核心线程处理</li>
 *  <li>如果队列满了，线程池线程（核心+非核心）也满了，就执行拒绝策略</li>
 *  <li>超时时间：是用来销毁非核心线程用的，计时开始的时候是它刚开始空闲</li>
 * </ul>
 * <h3>newFixedThreadPool(2)</h3>
 * <p>1.只有核心线程与无界队列：不会有线程销毁的问题，
 * 任务没有核心线程处理就丢在队列中，任务处理完了，
 * 线程一直占用资源，一直闲着,阿里不推荐的愿意是：无解队列会导致OOM（内存不足）</p>
 *
 * <h3>newCachedThreadPool</h3>
 * <p>没有核心线程，队列是SynchronousQueue，可以理解为容量为0的一个阻塞队列
 *  任务来了，就放到队列中，直接创建线程去处理，线程空闲60秒后会销毁
 *  不停有任务来，就可能不停的创建线程，任务都执行完毕，可能就有很多的线程空闲
 *  最后可能池里没有一个线程。阿里不推荐使用，原因是：创建的线程最大数量是整数的最大值
 *  因为每一个线程都要消耗大约1m的内存，是不可能创建那么多线程的，所以，真的任务很多，一定会产生OOM
 * </p>
 */
public class Main {
    public static void main(String[] args) throws Exception {
        //poolBasic();
        //poolCreated();
        poolShutdown();
    }

    private static void poolShutdown() {
        ExecutorService pool = Executors.newFixedThreadPool(2);

        pool.submit(() -> {
            try {
                System.out.println("---before----");
                TimeUnit.SECONDS.sleep(3);
                System.out.println("---after----");

            } catch (InterruptedException e) {
                e.printStackTrace();
                //shutdownNow会对正在执行的任务采用中断的方式来终止执行
                //所以我们自己的任务如果有对中断有响应就能正确关闭
                //如果没有响应，任务依然会继续执行
            }
        });
        //关闭线程池，但此时如果有任务还在执行，就等他执行完毕，如果有新的任务提交不会被接受
        //shutdown是非阻塞的
        pool.shutdown();
        System.out.println("done---");
        //立即关闭，看上面的捕获异常代码的注释
        //pool.shutdownNow();
    }

    private static void poolCreated() {
        // ExecutorService pool = Executors.newSingleThreadExecutor();
        // ExecutorService pool = Executors.newFixedThreadPool(2);

        //1.假定corePoolSize =2,maximumPoolSize = 10,BlockingQueue.size=3
        //2.提交一个任务，直接创建一个新的线程来执行这个任务，因为此时还没有达到corePoolsize的设定
        //  即便此时有一个线程，而且也还没有达到corePoolsize的设定，那么也会创建一个线程来执行任务
        // 也就是：只要线程的数量没有达到corePoolSize，有新任务来就直接创建线程来执行

        //3. 再有新任务就放到阻塞队列中，直到队列满
        //4.在这中间如果有任务已经执行完毕，这些闲下来的线程会去任务队列中提取任务来执行
        //5.任务执行完一些，就会有一些空闲的线程，那么这些空闲的线程在等待设定的空闲时间之后会被销毁
        //6.销毁到只剩下corepoolsize设定大小的线程数量后就不再销毁

        // 说明：Executors返回的线程池对象的弊端如下：
        //   1）FixedThreadPool和SingleThreadPool:
        //允许的请求队列长度为Integer.MAX_VALUE，可能会堆积大量的请求，从而导致OOM （OutOfMemoryError）
        //  2）CachedThreadPool:
        //允许的创建线程数量为Integer.MAX_VALUE，可能会创建大量的线程，从而导致OOM。

        //队列长度是一个很大的值，比如Integer.MAX_VALUE这样可以认为队列是一个无界队列

        //====所以阿里巴巴编码规范中，不建议用Executors来创建线程池==
        //  线程池不允许使用Executors去创建，而是通过ThreadPoolExecutor的方式，
        //  这样的处理方式让写的同学更加明确线程池的运行规则，规避资源耗尽的风险。
        ExecutorService pool = Executors.newCachedThreadPool();
        //建议在最大线程数量和队列数量上不要搞无界。
        ThreadPoolExecutor executor = new ThreadPoolExecutor(2,
                6, 1,
                TimeUnit.MINUTES, new LinkedBlockingQueue<>(48));
        Runnable r1 = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 1");
        };

        Runnable r2 = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 2");
        };

        Runnable r3 = () -> {
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " 3");
        };
        //提交任务过来：1.立即分配线程执行，
        // 2，线程不够可以创建额外的出来去执行
        //3.先把提交的任务存起来，等其它任务执行完毕之后有空闲的线程就可以执行这个任务了。
        //4.直接拒绝，不执行这个提交的任务。

        pool.submit(r1);
        pool.submit(r2);
        pool.submit(r3);
    }

    private static void poolBasic() throws InterruptedException, ExecutionException {
        //线程关联的run执行结束就自动销毁，
        // 或者利用中断而且你自己的run方法也能响应中断

        //一个线程执行时是在运行关联的run方法，如果run方法不执行完，线程是不能干别的事情的

        //创建了一个线程池
        ExecutorService pool = Executors.newFixedThreadPool(2);
        Runnable runnable = () -> System.out.println("hello pool");
        pool.submit(runnable);

        Callable<String> callable = () -> {
            TimeUnit.SECONDS.sleep(3);
            System.out.println("执行");
            return "这是一个返回数据的接口";
        };
        Future<String> future = pool.submit(callable);
        System.out.println("干别的事情-----");

        //get方法是一个阻塞的方法，线程任务没有执行完毕就一直卡在这里
        //get是运行在主线程（案例中）
        String result = future.get();

        System.out.println("after get----");
        System.out.println(result);
    }
}
