package ch05;

import java.util.concurrent.*;

import static util.ThreadUtils.sleep;


/**
 * 线程池是{@link ExecutorService}接口的实现类，它的submit方法是支持{@link Runnable}与{@link Callable}的
 * 处理2个不同的接口会比较麻烦，所以在其抽象实现类{@link AbstractExecutorService}里对{@link Runnable}与{@link Callable}
 * 接口进行了转换处理，都把其转换为了{@link FutureTask},也就是说线程池里处理的任务其实{@link FutureTask}.
 * <h3>Runnable转换为Callable</h3>
 * 在{@link FutureTask#FutureTask(Runnable, Object)}里可以看到其调用{@link Executors#callable(Runnable)}方法
 * 来实现Runnable转换为Callable功能的，主要就是靠{@link Executors}的内部类 Executors.RunnableAdapter实现的
 * <h3>FutureTask</h3>
 * <p> FutureTask类是一个具体的类，它实现了{@link RunnableFuture}接口，此接口继承了{@link Runnable}与{@link Future}接口。
 * {@link FutureTask}的构造函数必须传一个{@link Runnable}或者{@link Callable}
 * 实例化时不管参数是Runnable还是Callable，内部都会转换为Callable，内部用一个类型为Callable的变量来存储构造函数传递来的数据，
 * 而FutureTask类本身又是{@link Runnable}的实现类，所以相当于通过{@link FutureTask}类型把所有的{@link Runnable}
 * 与{@link Callable}都转换为了FutureTask，而{@link Executor}({@link ExecutorService}是其子接口)执行任务都是针对{@link Runnable},
 * 这样就把{@link Runnable},{@link Callable}与线程池结合起来了</p>
 * <h3>基本功能</h3>
 * <ul>
 *     <li>利用{@link FutureTask#isDone()}来判断任务是否完成</li>
 *     <li>利用{@link FutureTask#isCancelled()}来判断任务是否已经取消</li>
 *     <li>利用{@link FutureTask#cancel(boolean)} 来取消任务</li>
 *     <li>利用{@link FutureTask#get()}获取异步任务的结果，任务没有结束，此方法会阻塞</li>
 * </ul>
 * <h3>参考资料</h3>
 * <ul>
 *     <li><a href="https://zhuanlan.zhihu.com/p/377296488">FutureTask简介</a></li>
 * </ul>
 * @see java.util.concurrent.ThreadPoolExecutor
 */
public class FutureTaskDemo {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        FutureTask task = new FutureTask(()->{
            sleep(3);
            System.out.println("runnable in future task");
        },null);

        ExecutorService pool = Executors.newCachedThreadPool();
        Future<?> future = pool.submit(task);
        // false:因为submit方法把参数task进行了包装，变成了一个新的FutureTask
        System.out.println("future == task = " + (future == task));
        // get方法是阻塞方法，在这个案例里get的返回值是实例化FutureTask时指定的null，你可以把null换成别的数据试试
        // 不管是用task还是future操作get方法结果都是一样的，没有本质区别
        System.out.println("task.get() = " + task.get());
    }
}
