package com.nf.mvc.chain;

public interface Interceptor {

    boolean pre();
    void post();
}
