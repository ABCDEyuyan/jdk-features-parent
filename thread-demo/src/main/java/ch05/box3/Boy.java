package ch05.box3;


import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;

public class Boy extends Thread {
    private BlockingQueue<Fruit> box;

    public Boy(BlockingQueue<Fruit> box) {
        this.box = box;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Fruit fruit = new Fruit();
                box.put(fruit);
                System.out.println("放了：" + fruit);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
