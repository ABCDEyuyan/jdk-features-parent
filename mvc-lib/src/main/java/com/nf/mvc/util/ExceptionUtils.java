package com.nf.mvc.util;

public interface ExceptionUtils {
    /**
     *
     * 此方法只要参数不是一个null的就不会返回一个null的异常
     * 其基本逻辑就是在异常链上找cause，直到null或不等于参数指定的异常为止，也就是找出最原始的异常
     * 反射调用会把任何方法调用抛出的异常用InvocationTargetException异常包装起来，想得到真正的异常需要调用getCause方法获取
     * 参考: https://stackoverflow.com/questions/6020719/what-could-cause-java-lang-reflect-invocationtargetexception
     * https://stackoverflow.com/questions/17747175/how-can-i-loop-through-exception-getcause-to-find-root-cause-with-detail-messa
     * @param ex
     * @return
     */
    public static Throwable getRootCause(Throwable ex){
        Throwable cause;
        Throwable result = ex;
        while(null != (cause = result.getCause())  && (result != cause) ) {
            result = cause;
        }
        return result;
    }
}
