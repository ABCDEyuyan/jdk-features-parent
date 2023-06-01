package ch05;

import java.util.concurrent.TimeUnit;

/**
 * 演示超时wait的用法
 */
public class WaitNotifyDemo3 {
    public synchronized void m1() {
        try {
            System.out.println("before wait----");
            //单位是毫秒，下面意思是wait 3秒
            this.wait(3000);
            System.out.println("after wait----");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    public synchronized void m2() {
        try {
            System.out.println("开始休眠");
            //sleep方法“不会"释放锁
            TimeUnit.SECONDS.sleep(10);
            System.out.println("休眠结束");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
