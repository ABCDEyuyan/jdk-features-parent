package ch08.active;

public class StarvationDemo implements Runnable{
    @Override
    public void run() {
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " Started.");
        synchronized(StarvationDemo.class) {
            System.out.println(threadName + " synchronized block.");
            while(true) { } // infinite loop
        }
        // System.out.println(threadName + " Ended.");
    }

    public static void main(String[] args) {
        System.out.println("Main thread Started");
        StarvationDemo s1 = new StarvationDemo();
        StarvationDemo s2 = new StarvationDemo();
        Thread t1 = new Thread(s1);
        Thread t2 = new Thread(s2);
        t1.start();
        t2.start();
        System.out.println("Main thread Ended");
    }
}
