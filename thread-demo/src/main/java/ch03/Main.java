package ch03;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        MyTask task = new MyTask(5);
        task.setSecond(6);

        Thread t = new Thread(task);
        t.start();

        //直接访问task对象的result可能是0，因为有可能线程没有执行完毕
        //所以想访问正确的结果就需要确保计算任务的线程能执行完毕
        t.join();
        System.out.println("线程：" +Thread.currentThread().getName()
                + " 结果：" + task.getResult());
    }
}
