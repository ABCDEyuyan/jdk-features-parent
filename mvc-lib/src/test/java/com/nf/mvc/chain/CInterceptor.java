package com.nf.mvc.chain;

public class CInterceptor implements Interceptor{
    @Override
    public boolean pre() {
        System.out.println("ccc 111111");
        return true;
    }

    @Override
    public void post() {
        System.out.println("ccc 22222");
    }
}
