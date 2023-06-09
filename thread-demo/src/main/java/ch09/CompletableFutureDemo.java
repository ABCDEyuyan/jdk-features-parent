package ch09;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static util.ThreadUtils.*;

/**
 * 使用Future获得异步执行结果时，要么调用阻塞方法get()，要么轮询看isDone()是否为true，
 * 这两种方法都会导致调用线程被迫等待，进入阻塞状态。
 * <p>
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
 * <h3>异步任务组合</h3>
 * <ul>
 *      <li>{@link CompletableFuture#anyOf(CompletableFuture[])}:任意一个任务执行完，就执行anyOf返回的CompletableFuture。
 *      如果执行的任务异常，anyOf的CompletableFuture，执行get方法，会抛出异常</li>
 *      <li>{@link CompletableFuture#allOf(CompletableFuture[])}:所有任务都执行完成后，才执行 allOf返回的CompletableFuture。
 *      如果任意一个任务异常，allOf的CompletableFuture，执行get方法，会抛出异常</li>
 *      <li>{@link CompletableFuture#thenCompose(Function)}:方法会在某个任务执行完成后，将该任务的执行结果,作为方法入参,去执行指定的方法。
 *      该方法会返回一个新的CompletableFuture实例 </li>
 *      <ul>and关系：将两个CompletableFuture组合起来，只有这两个都正常执行完了，才会执行某个任务。
 *          <li>{@link CompletableFuture#thenCombine(CompletionStage, BiFunction)} 开头的3个方法:
 *       会将两个任务的执行结果作为方法入参，传递到指定方法中，且有返回值</li>
 *          <li>{@link CompletableFuture#thenAcceptBoth(CompletionStage, BiConsumer)}开头的3个方法:
 *       会将两个任务的执行结果作为方法入参，传递到指定方法中，且无返回值</li>
 *          <li>{@link CompletableFuture#runAfterBoth(CompletionStage, Runnable)}开头的3个方法:
 *       不会把执行结果当做方法入参，且没有返回值</li>
 *      </ul>
 *      <ul>or关系:将两个CompletableFuture组合起来，只要其中一个执行完了,就会执行某个任务。
 *          <li>{@link CompletableFuture#applyToEither(CompletionStage, Function)} 开头的3个方法：
 *          会将已经执行完成的任务，作为方法入参，传递到指定方法中，且有返回值</li>
 *          <li>{@link CompletableFuture#acceptEither(CompletionStage, Consumer)} 开头的3个方法
 *          会将已经执行完成的任务，作为方法入参，传递到指定方法中，且无返回值</li>
 *          <li>{@link CompletableFuture#runAfterEither(CompletionStage, Runnable)} 开头的3个方法
 *          不会把执行结果当做方法入参，且没有返回值</li>
 *      </ul>
 *
 * </ul>
 * <h3>参考资料</h3>
 * <ul>
 *     <li><a href="https://zhuanlan.zhihu.com/p/344431341">CompletableFuture用法详解</a></li>
 *     <li><a href="https://juejin.cn/post/6970558076642394142">CompletableFuture详解</a></li>
 *     <li><a href="https://www.liaoxuefeng.com/wiki/1252599548343744/1255943750561472">多线程的入门教程</a></li>
 * </ul>
 */
public class CompletableFutureDemo {
    public static void main(String[] args) throws Exception {
        //        demo1();
        //        thenApplyTest();
        //        allOfTest();
        //        anyOfTest();
        thenComposeTest();
        //        thenCombineTest();
        // 主线程不要立刻结束，否则CompletableFuture默认使用的线程池会立刻关闭:
        sleep(5);
    }

    public static void demo1() {
        // 创建异步执行任务:
        CompletableFuture<String> cf = CompletableFuture.supplyAsync(CompletableFutureDemo::getSth);
        // 如果执行成功:
        cf.whenComplete((result, e) -> {
            //不是在主线程执行下面的代码，仍然是CompletableFuture使用的ForkJoinPool里的线程
            System.out.println("----when complete---");
            printThreadName();
            System.out.println("---result: " + result + "---ex:" + e.getMessage());
        });
        //exceptionally是专门用来处理执行过程中的异常的
        cf.exceptionally(e -> {
            System.out.println("---exceptionally---");
            printThreadName();
            System.out.println("ex:" + e.getMessage());
            return null;
        });
    }

    public static void thenApplyTest() {
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

    public static void thenApplyTest2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return 100;
        });
        CompletableFuture<String> f = future.thenApplyAsync(i -> i * 10).thenApply(i -> i.toString());

        System.out.println(f.get()); //"1000"
    }

    public static void thenComposeTest2() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            return 100;
        });
        CompletableFuture<String> f = future.thenCompose( i ->
             CompletableFuture.supplyAsync(() -> (i * 10) + ""));

        System.out.println(f.get()); //1000
    }
    public static void allOfTest() {
        CompletableFuture<Void> a = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("a 我执行完了");
        });
        CompletableFuture<Void> b = CompletableFuture.runAsync(() -> {
            System.out.println("b 我执行完了");
        });
        CompletableFuture<Void> anyOfFuture = CompletableFuture.allOf(a, b).whenComplete((f, s) -> {
            System.out.println("finish " + "第一个参数:" + f + " 第二个参数:" + s);
        });
        anyOfFuture.join();

    }

    public static void anyOfTest() {
        CompletableFuture<Void> a = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(3000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("a 我执行完了");
        });
        CompletableFuture<Void> b = CompletableFuture.runAsync(() -> {
            System.out.println("b 我执行完了");
        });
        CompletableFuture<Object> anyOfFuture = CompletableFuture.anyOf(a, b).whenComplete((f, s) -> {
            System.out.println("finish " + "第一个参数:" + f + " 第二个参数:" + s);
        });
        anyOfFuture.join();

    }

    public static void thenComposeTest() {
        CompletableFuture<List<String>> total = CompletableFuture.supplyAsync(() -> {
            System.out.println("---1---" + threadName());
            // 第一个任务获取美术课需要带的东西，返回一个list
            List<String> stuff = new ArrayList<>();
            stuff.add("画笔");
            stuff.add("颜料");
            return stuff;
        }).thenCompose(list -> {
            System.out.println("---2---" + threadName());
            // 向第二个任务传递参数list(上一个任务美术课所需的东西list)
            CompletableFuture<List<String>> insideFuture = CompletableFuture.supplyAsync(() -> {
                List<String> stuff = new ArrayList<>();
                // 第二个任务获取劳技课所需的工具
                stuff.add("剪刀");
                stuff.add("折纸");
                // 合并两个list，获取课程所需所有工具
                List<String> allStuff = Stream.of(list, stuff).flatMap(Collection::stream).collect(Collectors.toList());
                return allStuff;
            });
            return insideFuture;
        });
        printThreadName();
        System.out.println(total.join().size());

    }

    /**
     * 测试时：两个任务可能在各自线程执行，也可能在同一个线程执行，顺序也是不确定的
     */
    public static void thenCombineTest() {
        CompletableFuture<List<String>> painting = CompletableFuture.supplyAsync(() -> {
            System.out.println("---1---" + threadName());
            // 第一个任务获取美术课需要带的东西，返回一个list
            List<String> stuff = new ArrayList<>();
            stuff.add("画笔");
            stuff.add("颜料");
            return stuff;
        });
        CompletableFuture<List<String>> handWork = CompletableFuture.supplyAsync(() -> {
            System.out.println("---2---" + threadName());
            // 第二个任务获取劳技课需要带的东西，返回一个list
            List<String> stuff = new ArrayList<>();
            stuff.add("剪刀");
            stuff.add("折纸");
            return stuff;
        });
        // 传入handWork列表，然后得到两个CompletableFuture的参数Stuff1和2
        CompletableFuture<List<String>> total = painting.thenCombine(handWork, (stuff1, stuff2) -> {
            System.out.println("---合并---" + threadName());
            // 合并成新的list
            List<String> totalStuff = Stream.of(stuff1, stuff2)
                    .flatMap(Collection::stream)
                    .collect(Collectors.toList());
            return totalStuff;
        });
        List<String> list = total.join();
        list.forEach(System.out::println);
    }

    private static void doSth() {
        sleep(1);
        System.out.println("done---");
    }

    private static String getSth() {
        sleep(1);
        System.out.println("get sth---");
        throw new RuntimeException("ex---");
        //return "get sth---";

    }
}
