package com.nf;

public class MyCloseable2 implements AutoCloseable{

    public void m1(){
        System.out.println("m1 -2222***");
        throw new RuntimeException("m1 in mYClosable");
    }
    @Override
    public void close() throws Exception {
        System.out.println("close2-----");
    }
}
