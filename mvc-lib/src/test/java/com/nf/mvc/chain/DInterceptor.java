package com.nf.mvc.chain;

public class DInterceptor implements Interceptor{
    @Override
    public boolean pre() {
        System.out.println("dd 111111");
        return true;
    }

    @Override
    public void post() {
        System.out.println("dd 22222");
    }
}
