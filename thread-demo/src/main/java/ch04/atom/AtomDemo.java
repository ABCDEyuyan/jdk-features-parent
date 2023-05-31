package ch04.atom;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomDemo {

    //默认初始值是0，也可以在构造函数中指定一个初始值
    private AtomicInteger atomicInteger = new AtomicInteger();

    public  void  incr(){
        for (int i = 0; i < 1000; i++) {
            //addAndGet相当于++i，getAndAdd相当于i++
            atomicInteger.addAndGet(1);
        }
    }

    public  void decre(){
        for (int i = 0; i < 1000; i++) {
            //decrementAndGet相当于--i,getAndDecrement相当于i--
            atomicInteger.decrementAndGet();
        }
    }

    public  int getCount(){
        return this.atomicInteger.get();
    }

}
