package ch06;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 任务调度
 */
public class Main2 {
    public static void main(String[] args) {
        ScheduledExecutorService pool = Executors.newScheduledThreadPool(2);
        Runnable runnable = () -> {
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(System.currentTimeMillis() + "----");
        };

        //1.初始延迟2秒之后执行，执行一次
        // pool.schedule(runnable, 2, TimeUnit.SECONDS);

        //2.以固定的频率调度任务执行,如果任务执行时间超过间隔的时间（这里是1秒）
        //那么不会出现任务同时在执行的问题，必须是等上一个任务执行完毕之后才开始任务的调度
        //此时此刻任务调度的间隔时间设定看上去象失效的
        System.out.println(System.currentTimeMillis());
        pool.scheduleAtFixedRate(runnable,
                2,
                1,
                TimeUnit.SECONDS);


        //3.以固定的延迟来反复调度任务
        //        pool.scheduleWithFixedDelay(runnable,
        //                2,
        //                1,
        //                TimeUnit.SECONDS);
        //怎么实现每天晚上12点干活？

    }
}
