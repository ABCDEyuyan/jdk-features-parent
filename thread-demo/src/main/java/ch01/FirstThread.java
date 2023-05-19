package ch01;

public class FirstThread extends Thread{
    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        int max = 1000000;
        int i = 0;
        while (i< max){
            System.out.println("0000000");
            i++;
        }
    }
}
