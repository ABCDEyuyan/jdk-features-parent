package ch05.box2;

import java.util.concurrent.TimeUnit;

public class Girl extends Thread{
    private Box2<Fruit> box;

    public Girl(Box2<Fruit> box){
        this.box = box;
    }
    @Override
    public void run() {

        while (true) {
            Fruit fruit = box.take();
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
