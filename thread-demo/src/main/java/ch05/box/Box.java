package ch05.box;

import java.util.LinkedList;

public class Box<T> {
    private LinkedList<T> box = new LinkedList<>();
    private int size;

    public Box(int size) {
        this.size = size;
    }

    public synchronized void put(T fruit) {
        if (box.size() < size) {
            System.out.println("放入：" + fruit);
            box.add(fruit);
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
