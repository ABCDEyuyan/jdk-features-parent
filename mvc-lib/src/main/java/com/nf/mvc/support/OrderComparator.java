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
        boolean o1HaveOrder = o1.getClass().isAnnotationPresent(Order.class);
        boolean o2HaveOrder = o2.getClass().isAnnotationPresent(Order.class);
        int o1Order = Integer.MAX_VALUE;
        int o2Order = Integer.MAX_VALUE;
        if (o1HaveOrder ) {
            Order o1Anno = o1.getClass().getDeclaredAnnotation(Order.class);
            o1Order = o1Anno.value();
        }
        if (o2HaveOrder ) {
            Order o2Anno = o2.getClass().getDeclaredAnnotation(Order.class);
            o2Order = o2Anno.value();
        }

        return  o1Order - o2Order;
    }
}
