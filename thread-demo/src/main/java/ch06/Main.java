package ch06;

import java.util.concurrent.*;

/**
 * 讲了
 * 1.线程池创建
 * 2.提交任务
 * 3.关闭线程池
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
