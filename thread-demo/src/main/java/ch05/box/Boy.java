package ch05.box;

import java.util.concurrent.TimeUnit;

public class Boy extends Thread{
    private Box<Fruit> box;
    public Boy(Box box) {
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
