package com.nf.mvc.support;

import java.util.Comparator;

public class OrderComparator<T> implements Comparator<T> {

    /**
     * 自定义组件可以不加注解，不加注解等于其Order值是整数最大值
     * 如果你加了Order注解，就以注解的指定的值为准
     *
     * 如果都不加注解，就是以扫描的顺序为准
     * @param o1
     * @param o2
     * @return
     */
    @Override
    public int compare(T o1, T o2) {
        int o1Order = getOrderValue(o1);
        int o2Order = getOrderValue(o2);
        return  o1Order - o2Order;
    }

    private int getOrderValue(T o){
        return o.getClass().isAnnotationPresent(Order.class)?
                o.getClass().getDeclaredAnnotation(Order.class).value():
                Integer.MAX_VALUE;

    }
}
