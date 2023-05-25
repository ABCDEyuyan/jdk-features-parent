package ch03;

/**
 * 数据是要传递给Thread对象或者Runnable的实现类
 *
 * 线程对象因为不是我们写的，是没有办法直接传递，
 * 但我们可以写一个类继承Thread，这样就往这个类添加一些东西
 *
 * 小结：
 * 传递数据给线程：
 *  1.构造函数
 *  2.普通的方法，比如setter
 *
 *  线程传递结果给别的线程
 *  1. 给共同的对象成员赋值，比如这里的MyTask对象的result
 *  2.
 */
public class MyTask2 implements Runnable{

    private  int first;
    private int second;

    public MyTask2() {
    }

    public MyTask2(int first) {
        this.first = first;
    }

    public int getSecond() {
        return second;
    }


    public void setSecond(int second) {
        this.second = second;
    }

    /**
     * 这个方法就是一个计算结果的处理方法
     *
     * 缺点：计算结果的处理逻辑完全由MyTask2自己来编写，不灵活
     * 别人就没法写第二种，第三种，第n种其它的处理逻辑
     * @param data
     */
    public void onComplete(int data) {
        System.out.println("-----"
                + Thread.currentThread().getName()
                + " 结果: " + data);
    }
    @Override
    public void run() {
        int sum = this.first + this.second;
        //有了计算的结果，准备交给别人去处理。
        //通过调用一个方法的形式去模拟交给别人处理
        onComplete(sum);
        System.out.println(sum + " 线程：" + Thread.currentThread().getName());
    }
}
