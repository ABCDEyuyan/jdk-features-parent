package com.nf.mvc;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class MapTest {
    @Test
    public void testMap(){
        Map<Integer, String> map = new HashMap<>();
        map.computeIfAbsent(1, s -> s.toString() + "ddd");
        System.out.println("map.get(1) = " + map.get(1));

        map.computeIfAbsent(1, s -> s.toString() + "ddd222");
        System.out.println("map.get(1) = " + map.get(1));//仍然是1ddd
    }

    @Test
    public void testNullMap(){
        Map<Integer, String> map = new HashMap<>();
        map.computeIfAbsent(1, s -> null);
        System.out.println("map.size() = " + map.size()); //0
        System.out.println("map.get(1) = " + map.get(1));

    }

    @Test
    public void testExceptionMap(){
        Map<Integer, String> map = new HashMap<>();
        map.computeIfAbsent(1, s -> {throw new RuntimeException("kong ---");});

    }

    @Test
    public void testExceptionPresentMap(){
        Map<Integer, String> map = new HashMap<>();
        map.computeIfPresent(1, (k,v) -> {throw new RuntimeException("kong ---");});

    }
}
