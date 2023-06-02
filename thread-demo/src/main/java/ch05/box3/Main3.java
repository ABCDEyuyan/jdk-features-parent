package ch05.box3;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class Main3 {
    public static void main(String[] args) {

        BlockingQueue<Fruit> blockingQueue = new LinkedBlockingQueue(3);
        Boy boy = new Boy(blockingQueue);
        Girl girl = new Girl(blockingQueue);

        boy.setName("boy");
        girl.setName("girl");

        boy.start();
        girl.start();


    }
}
