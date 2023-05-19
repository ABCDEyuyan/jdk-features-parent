package ch05.box2;

import java.util.concurrent.TimeUnit;

public class Girl extends Thread{
    private Box2<Fruit> box;
    public Girl(Box2 box) {
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
