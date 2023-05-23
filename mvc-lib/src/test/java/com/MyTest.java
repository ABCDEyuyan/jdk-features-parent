package com;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MyTest {
    private long counter;

    private void wasCalled() {
        counter++;
    }
    @Test
    public void ss(){
        List<String> list = Arrays.asList("abc1", "abc2", "abc3");
        counter = 0;
        Stream<String> stream = list.stream().filter(element -> {
            wasCalled();
            return element.contains("2");
        });
        System.out.println(counter);
    }

    @Test
    public void ss2(){
        List<String> list = Arrays.asList("abc1", "abc2", "abc3");
        counter = 0;
         list.stream().filter(element -> {
            System.out.println("filter() was called");
            //return element.contains("2");
            return true;
        }).map(element -> {
            System.out.println("map() was called");
            return element.toUpperCase();
        }).limit(4).collect(Collectors.toList());
    }
}
