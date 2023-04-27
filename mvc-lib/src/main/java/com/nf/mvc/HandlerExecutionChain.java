package com.nf.mvc;

import com.nf.mvc.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class HandlerExecutionChain {
    private final Object handler;

    private final List<HandlerInterceptor> interceptorList = new ArrayList<>();

    private int interceptorIndex = -1;

    public HandlerExecutionChain(Object handler) {
        this(handler, (HandlerInterceptor[]) null);
    }


    public HandlerExecutionChain(Object handler,  HandlerInterceptor... interceptors) {
        this(handler, (interceptors != null ? Arrays.asList(interceptors) : Collections.emptyList()));
    }

    public HandlerExecutionChain(Object handler, List<HandlerInterceptor> interceptorList) {
        this.handler = handler;
        this.interceptorList.addAll(interceptorList);
    }


    public Object getHandler() {
        return this.handler;
    }

    public void addInterceptor(HandlerInterceptor interceptor) {
        this.interceptorList.add(interceptor);
    }


    public void addInterceptor(int index, HandlerInterceptor interceptor) {
        this.interceptorList.add(index, interceptor);
    }


    public void addInterceptors(HandlerInterceptor... interceptors) {
        CollectionUtils.mergeArrayIntoCollection(interceptors,interceptorList);
    }


    public HandlerInterceptor[] getInterceptors() {
        return (!this.interceptorList.isEmpty() ? this.interceptorList.toArray(new HandlerInterceptor[0]) : null);
    }


    public List<HandlerInterceptor> getInterceptorList() {
        return (!this.interceptorList.isEmpty() ? Collections.unmodifiableList(this.interceptorList) :
                Collections.emptyList());
    }


}
