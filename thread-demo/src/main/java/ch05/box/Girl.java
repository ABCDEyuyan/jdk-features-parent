package ch05.box;

import java.util.concurrent.TimeUnit;

public class Girl extends Thread{
    private Box<Fruit> box;
    public Girl(Box box) {
        this.box = box;
    }

    @Override
    public void run() {
        while (true) {
            Fruit fruit = box.take();
            //System.out.println("取了:" + fruit);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
