package ch05.box2;

import java.util.concurrent.TimeUnit;

public class Boy extends Thread{
    private Box2<Fruit> box;
    public Boy(Box2 box) {
        this.box = box;
    }

    @Override
    public void run() {
        while (true) {
            box.put(new Fruit());
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
