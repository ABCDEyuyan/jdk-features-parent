package ch05.box;

import java.util.LinkedList;

public class Box<T> {
    private LinkedList<T> list = new LinkedList();
    private int capacity = 0;

    public Box(int capacity) {
        this.capacity = capacity;
    }

    public synchronized void put(T fruit) {
        while (list.size() >= capacity) {
            try {
                System.out.println("满了---");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.add(fruit);
        System.out.println(Thread.currentThread().getName() + " 加了：" + fruit);
        //加了水果之后也要通知取得人有东西取了。
        this.notifyAll();
    }

    public synchronized T take(){
        while (list.size() <= 0) {
            try {
                System.out.println("空了---");
                this.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        T fruit = list.removeFirst();
        System.out.println(Thread.currentThread().getName() + " 取出:" + fruit);
       //除非深思熟虑，否则就尽量的用notifyAll，不要用notify

        this.notifyAll();
        return fruit;
    }
    
}
