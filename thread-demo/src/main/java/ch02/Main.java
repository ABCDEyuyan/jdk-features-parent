package ch02;

import java.util.stream.IntStream;

public class Main {
    static long result = 0;
    public static void main(String[] args) throws InterruptedException {
        //joinDemo();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (long i = 0; i < 1000000; i++) {
                    result +=i;
                }
            }
        };

        Thread t = new Thread(runnable);
        t.start();
        t.join();
        System.out.println("最终的结果：" + result);

    }

    private static void joinDemo() {
        Runnable runnable = Main::myRun;
        Thread thread = new Thread(runnable);
        thread.start();

        try {
            //让当前执行的线程（Main）等待thread对象代表的线程执行完毕
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("done---");
    }


    static void myRun(){
        IntStream.range(0, 1000)
                .forEach(System.out::println);
    }
}
