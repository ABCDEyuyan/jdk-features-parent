package com.nf.mvc.util;

public abstract class ReflectionUtils {
    /**
     * 现在这种写法是默认调用class的默认构造函数来实例化对象的
     * 暂时没有考虑调用其它构造函数的情况
     * @param clz
     * @return
     */
    public static Object newInstance(Class<?> clz){
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }
}
