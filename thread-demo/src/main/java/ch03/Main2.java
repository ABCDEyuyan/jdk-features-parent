package ch03;

public class Main2 {
    public static void main(String[] args) {
        MyTask2 task2 = new MyTask2(10);
        task2.setSecond(20);

        Thread t = new Thread(task2);
        t.start();
    }
}
