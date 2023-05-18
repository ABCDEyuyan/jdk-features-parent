package ch04;

/**
 * synchronized关键字可以用在3个地方
 * 1 静态方法：类的class对象MyCounter3.class
 * 2. 实例方法 ，锁对象this
 * 3 同步代码块  ，可以自由指定
 */
public class MyCounter3 {
    private  int count =0;

    private Object lock = new Object();
    public  void  incr(){

        synchronized (this) {
            count++;
        }
    }

    public  void decre(){
        synchronized (lock) {
            count--;
        }
    }

    public  int getCount(){
        return this.count;
    }


}
