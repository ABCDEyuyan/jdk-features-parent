package ch03;

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
 * 2.
 */
public class MyTask implements Runnable {

    private int first;
    private int second;

    private int result;


    public MyTask() {
    }

    //方法一：通过构造函数传递数据
    public MyTask(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }

    //方法二：通过方法传递数据，一般是setter方法
    public void setSecond(int second) {
        this.second = second;
    }

    public int getResult() {
        return result;
    }

    @Override
    public void run() {
        int sum = this.first + this.second;
        this.result = sum;
        System.out.println(sum + " 线程：" + Thread.currentThread().getName());
    }
}
