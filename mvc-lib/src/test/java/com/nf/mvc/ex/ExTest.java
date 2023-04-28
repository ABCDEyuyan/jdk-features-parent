package com.nf.mvc.ex;

import org.junit.Test;

public class ExTest {
    @Test
    public void testSimple(){
        ForEx forEx = new ForEx();
        try {
            forEx.m2();
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            System.out.println("exception.getCause() = " + exception.getCause());
        }
    }

    @Test
    public void testSimple2(){
        ForEx forEx = new ForEx();
        try {
            forEx.m3();
        } catch (Exception exception) {
            System.out.println("exception = " + exception);
            System.out.println("exception.getCause() = " + exception.getCause());
        }
    }


}
