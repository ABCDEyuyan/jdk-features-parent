package ch04;

public class MyCounter {
    private  int count =0;

    //incr与decre由于都是MyCounter的实例方法
    //synchronized获取的都是MyCounter这一个对象的锁

    //能进到此方法，就表示获取了锁，方法执行完毕就自动解锁，解锁之后别的线程才有机会重新获取锁
    public synchronized void  incr(){
        count++;
    }

    public synchronized void decre(){
        count--;
    }

    public  int getCount(){
        return this.count;
    }


}
