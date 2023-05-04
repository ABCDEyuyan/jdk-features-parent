package com.nf.mvc.chain;

public class BInterceptor implements Interceptor{
    @Override
    public boolean pre() {
        System.out.println("bbb 111111");

        return true;
    }

    @Override
    public void post() {
        System.out.println("bbb 22222");
    }
}
