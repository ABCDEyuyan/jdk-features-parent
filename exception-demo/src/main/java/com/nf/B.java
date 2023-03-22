package com.nf;

public class B {
    public void m1(){
        C c = new C();
        try {
            c.m1();
        } catch (RuntimeException e) {
            throw new IllegalArgumentException("b--->c(no e) m1 argument");
        }
    }
}
