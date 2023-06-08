package ch09;

import java.util.concurrent.CompletableFuture;
import java.util.function.*;

import static util.ThreadUtils.printThreadName;
import static util.ThreadUtils.sleep;

/**
 * 使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否为true，
 * 这两种方法都会导致调用线程被迫等待，进入阻塞状态。
 *
 * 从Java 8开始引入了CompletableFuture，它针对Future做了改进，
 * 可以传入回调对象，当异步任务完成或者发生异常时，自动调用回调对象的回调方法。
 * <h3>实例化</h3>
 * <ul>
 *     <li>{@link CompletableFuture#runAsync(Runnable)}：无返回值</li>
 *     <li>{@link CompletableFuture#supplyAsync(Supplier)}:有返回值 </li>
 * </ul>
 * <h3>获取结果</h3>
 * <ul>
 *     <li>{@link CompletableFuture#get()}:阻塞式的获取结果</li>
 *     <li>{@link CompletableFuture#whenComplete(BiConsumer)}的三个方法与{@link CompletableFuture#exceptionally(Function)}</li>
 *     <li>{@link CompletableFuture#handle(BiFunction)}开头的3个方法，它与complete开头的处理结果的区别是，它可以改变返回结果</li>
 *     <li>{@link CompletableFuture#thenApply(Function)}开头的3个方法,apply方法和handle方法一样，都是结束计算之后的后续操作，
 *     唯一的不同是，handle方法会给出异常，可以让用户自己在内部处理，而apply方法只有一个返回结果，如果异常了，会被直接抛出，交给上一层处理</li>
 *     <li>{@link CompletableFuture#thenAccept(Consumer)}开头的3个方法,只做最终结果的消费，注意此时返回的CompletableFuture是空返回</li>
 *     <li>{@link CompletableFuture#exceptionally(Function)} }:捕捉到所有中间过程的异常</li>
 * </ul>
 * <h3>参考资料</h3>
 * <ul>
 *     <li><a href="https://zhuanlan.zhihu.com/p/344431341">CompletableFuture用法详解</a></li>
 * </ul>
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception{
       demo1();
     //  demo2();

        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        sleep(5);
    }

    public static void demo1(){
        // 创建异步执行任务:
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(CompletableFutureDemo::getSth);
        // 如果执行成功:
        cf.whenComplete((result,e) -> {
            //不是在主线程执行下面的代码，仍然是CompletableFuture使用的ForkJoinPool里的线程
            System.out.println("----when complete---");
            printThreadName();
            System.out.println("---result: " + result + "---ex:" + e.getMessage());
        });
        //exceptionally是专门用来处理执行过程中的异常的
        cf.exceptionally(e->{
            System.out.println("---exceptionally---");
            printThreadName();
            System.out.println("ex:" + e.getMessage());
            return null;
        });
    }

    public static void demo2(){
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            System.out.println("1:");
           printThreadName();
           sleep(1);
            return "111";
        }).thenApply(s -> {
            System.out.println("2:" + s);
            printThreadName();
            return "222";
        });
    }

    private static void doSth(){
        sleep(1);
        System.out.println("done---");
    }

    private static String getSth(){
        sleep(1);
        System.out.println("get sth---");
        throw new RuntimeException("ex---");
        //return "get sth---";

    }
}
