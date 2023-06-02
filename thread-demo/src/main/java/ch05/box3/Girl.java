package ch05.box3;

import java.util.concurrent.BlockingQueue;

public class Girl extends Thread{
    private BlockingQueue<Fruit> box;
    public Girl(BlockingQueue<Fruit> box) {
        this.box = box;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Fruit fruit = box.take();
                System.out.println("取了:" + fruit + " count:" + box.size());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

           /* try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
    }
}
