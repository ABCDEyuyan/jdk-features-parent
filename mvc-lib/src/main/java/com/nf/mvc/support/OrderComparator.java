package com.nf.mvc.support;

import java.util.Comparator;

public class OrderComparator<T> implements Comparator<T> {

    /**
     * 这里的实现要用减号，不要用类似o1Order>o2Order?1:-1这种写法，因为不满足自反性，
     * 见alibabajava开发手册的说明
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
