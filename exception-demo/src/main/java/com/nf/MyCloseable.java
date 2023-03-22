package com.nf;

public class MyCloseable implements AutoCloseable{

    public void m1(){
        System.out.println("m1 -***");
        throw new RuntimeException("m1 in mYClosable");
    }
    @Override
    public void close() throws Exception {
        System.out.println("close-----");
    }
}
