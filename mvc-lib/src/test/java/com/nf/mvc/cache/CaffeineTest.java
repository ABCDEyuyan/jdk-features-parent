package com.nf.mvc.cache;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.junit.Test;

public class CaffeineTest {
    @Test
    public void testCaffeine() {
        LoadingCache<String, String> cache = Caffeine.newBuilder()
                // 最大容量为1
                .maximumSize(1)
                .build(k -> getValue(k));
        cache.put("1", "111");
        cache.put("2", "222");
        cache.put("3", "333");
        cache.cleanUp();
        System.out.println(cache.getIfPresent("1"));
        System.out.println(cache.getIfPresent("2"));
        System.out.println(cache.getIfPresent("3"));
    }

    private static String getValue(String k) {
        return k + ":value";
    }
}
