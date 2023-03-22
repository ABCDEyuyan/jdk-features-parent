package com.nf;

public class A {
    public void m1(){
        C c = new C();
        try {
            c.m1();
        } catch (RuntimeException e) {
            throw new IllegalStateException("a(m1)--->c(with e)  state",e);
        }
    }
}
