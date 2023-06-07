package ch08;

import java.util.UUID;
import java.util.concurrent.Semaphore;

/**
 * Semaphore:信号量的意思，主要用来同时有多少个线程可以访问资源,
 * 比如new Semaphore(3),相当于有3个通行证，最多只有3个线程能同时访问资源。
 * <p>调用acquire用来获取通行证(通行证数量-1)，调用release相当于释放通行证(通行证数量+1)</p>
 */
public class SemaphoreDemo {
    // 任意时刻仅允许最多3个线程获取许可:
    final Semaphore semaphore = new Semaphore(3);

    public String access() throws Exception {
        // 如果超过了许可数量,其他线程将在此等待:
        semaphore.acquire();
        try {
            return UUID.randomUUID().toString();
        } finally {
            semaphore.release();
        }
    }
}
