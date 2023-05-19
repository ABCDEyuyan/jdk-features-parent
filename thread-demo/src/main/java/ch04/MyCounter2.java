package ch04;

public class MyCounter2 {
    private  int count =0;

    //incr与decre由于都是MyCounter的实例方法
    //synchronized获取的都是MyCounter这一个对象的锁

    //能进到此方法，就表示获取了锁，方法执行完毕就自动解锁，解锁之后别的线程才有机会重新获取锁
    public  void  incr(){
        //这种写法叫同步代码块，跟MyConter的写法是等价的
        //锁对象都是this
        synchronized (this) {
            count++;
        }
    }

    public  void decre(){
        synchronized (this) {
            count--;
        }
    }

    public  int getCount(){
        return this.count;
    }


}
