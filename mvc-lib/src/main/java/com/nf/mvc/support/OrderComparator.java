package com.nf.mvc.support;

import java.util.Comparator;

public class OrderComparator<T> implements Comparator<T> {
    @Override
    public int compare(T o1, T o2) {
        boolean o1HaveOrder = o1.getClass().isAnnotationPresent(Order.class);
        boolean o2HaveOrder = o2.getClass().isAnnotationPresent(Order.class);
        if (o1HaveOrder == false || o2HaveOrder == false) {
            throw new IllegalArgumentException("类型上必须添加Order注解，并指定值，小的优先级高");
        }
        //表明2个类上都有注解
        Order o1Anno = o1.getClass().getDeclaredAnnotation(Order.class);
        Order o2Anno = o2.getClass().getDeclaredAnnotation(Order.class);

        return  o1Anno.value() - o2Anno.value();
    }
}
