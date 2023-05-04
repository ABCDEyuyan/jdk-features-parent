package com.nf.mvc.core;

import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class HashCodeAndEqualsTest {
    @Test
    public void testSimple(){
        Data data1 = new Data(1,2);
        Data data2 = new Data(1,2);
        Data data3 = new Data(2,4);
        Set<Data> set = new HashSet<>();
        set.add(data1);
        set.add(data2);
        set.add(data3);
        for (Data data : set) {
            System.out.println(data);
        }
    }
}
