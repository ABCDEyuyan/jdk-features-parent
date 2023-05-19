package ch04;

public class MyTask implements Runnable{
    //volatile解决了可见性的问题
    //还可以解决有序性的问题，利用了内存屏障（memory barrier）
    // 但是它“不能” 解决原子性的问题
    public volatile boolean stop = false;
    @Override
    public void run() {
        while (stop==false){
            //不要写下面的代码，因为println是一个synchronize方法，它解决了可见性的问题

           // System.out.println("-----");

        }

    }
}
