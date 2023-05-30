package ch04.atom;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomDemo {

    //默认初始值是0
    private AtomicInteger atomicInteger = new AtomicInteger();

    public  void  incr(){
        for (int i = 0; i < 1000; i++) {
            atomicInteger.addAndGet(1);
        }

    }

    public  void decre(){
        for (int i = 0; i < 1000; i++) {
            atomicInteger.addAndGet(-1);
        }


    }

    public  int getCount(){
        return this.atomicInteger.get();
    }

}
