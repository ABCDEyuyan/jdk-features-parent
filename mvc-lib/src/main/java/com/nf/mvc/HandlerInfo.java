package com.nf.mvc;

public class HandlerInfo {
    private Class<?> clz ;

    public HandlerInfo(Class<?> clz) {
        this.clz = clz;
    }

    public Class<?> getClz() {
        return clz;
    }
}
