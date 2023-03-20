package com.phase6;

import com.phase1.A;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) throws Exception {
        Class<A6> clz = A6.class;
        Method m1 = clz.getDeclaredMethod("m1");
            SixAnno anno = m1.getDeclaredAnnotation(SixAnno.class);

            System.out.println("anno.value() = " + anno.value());
            System.out.println("anno.name() = " + anno.name());


    }
}
