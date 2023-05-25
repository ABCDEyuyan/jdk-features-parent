package ch03;

import java.util.function.IntConsumer;

/**
 * 数据是要传递给Thread对象或者Runnable的实现类
 * <p>
 * 线程对象因为不是我们写的，是没有办法直接传递，
 * 但我们可以写一个类继承Thread，这样就往这个类添加一些东西
 * <p>
 * 小结：
 * 传递数据给线程：
 * 1.构造函数
 * 2.普通的方法，比如setter
 * <p>
 * 线程传递结果给别的线程
 * 1. 给共同的对象成员赋值，比如这里的MyTask对象的result
 * 2.通过回调方法的形式，比如MyTask3的构造函数传递的消费者回调方法
 */
public class MyTask3 implements Runnable {

    private int first;
    private int second;
    //函数结构---》只有一个方法--》赋值后就可以理解为一段代码
    //这段代码就是结果的处理逻辑
    //这样就比MyTask2灵活，原因就是计算结果的处理逻辑是别人传递过来的
    //也就表明别人想传递就传递什么，我MyTask3只负责调用它

    //注意点：处理的逻辑是运行在关联MyTask3这个Runnable的线程中，
    // 在这个案例中，此任务处理方法就是运行在Thread-0线程中，而不是main线程
    private IntConsumer consumer;

    public MyTask3() {
    }

    public MyTask3(int first) {
        this.first = first;
    }

    public MyTask3(int first, IntConsumer consumer) {
        this.first = first;
        this.consumer = consumer;
    }

    public int getSecond() {
        return second;
    }


    public void setSecond(int second) {
        this.second = second;
    }


    @Override
    public void run() {
        int sum = this.first + this.second;
        if (consumer != null) {
            consumer.accept(sum);
        }
        System.out.println(sum + " 线程：" + Thread.currentThread().getName());
    }
}
