package com.nf.mvc.ex;

public class ForEx {
    public void m1(){
        int i = 5;
        int j = 0;
        System.out.println(i / j);
    }

    public void m2(){
        m1();
    }

    public void m3(){
        try {
            m1();
        } catch (Exception e) {
            throw new RuntimeException("asdfasd");
           // throw new RuntimeException(e);
        }
    }
}
