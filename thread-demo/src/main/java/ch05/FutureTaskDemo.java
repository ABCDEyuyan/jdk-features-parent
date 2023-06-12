package ch05;

import java.util.concurrent.*;


/**
 * 线程池是{@link ExecutorService}接口的实现类，它的submit方法是支持{@link Runnable}与{@link Callable}的
 * 处理2个不同的接口会比较麻烦，所以在其抽象实现类{@link AbstractExecutorService}里对{@link Runnable}与{@link Callable}
 * 接口进行了转换处理，都把其转换为了{@link FutureTask},也就是说线程池里处理的任务其实{@link FutureTask}.
 * <h3>Runnable转换为Callable</h3>
 * 在{@link FutureTask#FutureTask(Runnable, Object)}里可以看到其调用{@link Executors#callable(Runnable)}方法
 * 来实现Runnable转换为Callable功能的，主要就是靠{@link Executors}的内部类{@link Executors.RunnableAdapter}实现的
 * <h3>FutureTask</h3>
 * <p> FutureTask类是一个具体的类，它实现了{@link RunnableFuture}接口，
 * 实例化时不管参数是Runnable还是Callable都转换为Callable， </p>
 * <h3>参考资料</h3>
 * <ul>
 *     <li><a href="https://juejin.cn/post/7031196740128768037">了解SynchronousQueue</a></li>
 * </ul>
 * @see java.util.concurrent.ThreadPoolExecutor
 */
public class FutureTaskDemo {
}
