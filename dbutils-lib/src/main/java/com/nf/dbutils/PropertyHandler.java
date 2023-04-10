package com.nf.dbutils;

/**
 * 在给实体类的属性赋值之前，把从数据库取出来的值进行一些处理
 * 比如string数据是不能直接赋值给一个枚举的，但通过这种机制
 * 就可以扩展框架的能力，让stirng可以赋值给枚举，当然，还有其它的一些应用
 */
public interface PropertyHandler {

    /**
     *
     * @param clz 实体类的setter方法的参数类型
     * @param value 从数据库里面取出来的值
     * @return
     */
     boolean support(Class<?> clz, Object value);

    Object apply(Class<?> clz, Object value);
}
