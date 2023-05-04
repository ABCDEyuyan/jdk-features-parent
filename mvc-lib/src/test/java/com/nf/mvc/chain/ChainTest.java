package com.nf.mvc.chain;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ChainTest {
    @Test
    public void testChain(){
        AInterceptor aInterceptor = new AInterceptor();
        BInterceptor bInterceptor = new BInterceptor();
        CInterceptor cInterceptor = new CInterceptor();
        DInterceptor dInterceptor = new DInterceptor();

        List<Interceptor> interceptors = new ArrayList<>();
        interceptors.add(aInterceptor);
        interceptors.add(bInterceptor);
        interceptors.add(cInterceptor);
        interceptors.add(dInterceptor);


        MyHandler handler = new MyHandler();

        Chain chain = new Chain();
        chain.setHandler(handler);
        chain.setInterceptors(interceptors);

        //模拟请求过来，进行处理
           boolean result =  chain.exePre();
        //1先执行拦截器的pre

        // 2接着是handler的处理
        if(result) {
            ((MyHandler) chain.getHandler()).process();
        }
        //3 拦截器的post部分

        chain.exePost();

    }
}
