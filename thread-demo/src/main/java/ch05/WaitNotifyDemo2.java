package ch05;

public class WaitNotifyDemo2 {
    public synchronized void m1(){
        try {
            System.out.println("before----");
            this.wait();
            System.out.println("after----");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void m2(){
        this.notify();
        System.out.println("after notify*****");
    }
}
