package ch07;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestHashMap {
    public static void main(String[] args) throws InterruptedException {
        Map map = new HashMap<String, Integer>(1000 * 10);

        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Runner t = new Runner(map);
            t.start();
            threads.add(t);
        }
        for (Thread thread : threads) {
            thread.join();
        }
        //下面最终输出的结果通常不是10000，
        // 这里说明了在多线程的情况下操作同一个map是有问题的
        System.out.println( map.size());

    }
}

class Runner extends Thread {
    Map map;

    Runner(Map map) {
        this.map = map;
    }

    @Override
    public void run() {
        for (int i = 0; i < 1000; i++) {
            map.put(this.getName() + i, i);
        }
    }

}