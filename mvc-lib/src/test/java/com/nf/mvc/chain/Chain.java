package com.nf.mvc.chain;

import java.util.List;

public class Chain {
    private Object handler;
    private List<Interceptor> interceptors;

    public Object getHandler() {
        return handler;
    }

    public void setHandler(Object handler) {
        this.handler = handler;
    }

    public List<Interceptor> getInterceptors() {
        return interceptors;
    }

    public void setInterceptors(List<Interceptor> interceptors) {
        this.interceptors = interceptors;
    }

    int index = -1;
    public boolean exePre(){

       /* for (Interceptor interceptor : interceptors) {
            interceptor.pre();
        }*/

        for (int i = 0; i < interceptors.size(); i++) {
            Interceptor interceptor = interceptors.get(i);

            if(interceptor.pre()==false){
                return false;
            }
            index = i;
        }

        return true;
    }

    public void exePost(){
        //像下面这样写，执行顺序达不到反序执行的效果
       /* for (Interceptor interceptor : interceptors) {
            interceptor.post();
        }*/

        for (int i = index; i >= 0; i--) {
            Interceptor interceptor = interceptors.get(i);
            interceptor.post();
        }
    }
}
