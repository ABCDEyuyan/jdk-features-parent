package com.nf.mvc.chain;

public class AInterceptor implements Interceptor{
    @Override
    public boolean pre() {
        System.out.println("a 111111");
        return true;
    }

    @Override
    public void post() {
        System.out.println("a 22222");
    }
}
