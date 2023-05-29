package ch05.box2;

import java.util.LinkedList;

/**
 * 这是书上的写法，有bug，没有考虑虚假唤醒的问题
 * 关于虚假唤醒(spurious wakeup)可以参看：
 * https://juejin.cn/post/6986292785221468167
 * 和https://www.cnblogs.com/dk1024/p/14163377.html (这个更好懂)
 * @param <T>
 */
public class Box2<T> {
    private LinkedList<T> box = new LinkedList<>();
    private int size;

    public Box2(int size) {
        this.size = size;
    }

    public synchronized void put(T fruit) {
        if (box.size() < size) {
            box.add(fruit);
            System.out.println("放入：" + fruit);
        } else {

            try {
                System.out.println("满了---");
                this.notifyAll();
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized T take() {
        T fruit = null;
        if (box.size() > 0) {
            fruit = box.removeFirst();
            System.out.println("取出:" + fruit);
        } else {

            try {
                System.out.println("空的---");
                this.notifyAll();
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return fruit;
    }
}
