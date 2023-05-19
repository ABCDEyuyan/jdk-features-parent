package ch04;

/**
 * 演示原子类的操作，简单的问题能用原子类型解决就用它
 */
public class Main5 {
    public static void main(String[] args) throws InterruptedException {

        AtomDemo atomDemo = new AtomDemo();

        Runnable r1 = ()->{
            atomDemo.incr();
        };

        Runnable r2 = ()->{
           atomDemo.decre();
        };
        Thread t1 = new Thread(r1, "t1");
        Thread t2 = new Thread(r2, "t2");

        t1.start();
        t2.start();


        t1.join();
        t2.join();

        System.out.println("count:" + atomDemo.getCount());


    }


}
