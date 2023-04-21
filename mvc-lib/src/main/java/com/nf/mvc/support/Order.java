package com.nf.mvc.support;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Order {
    //默认值是整数的最大值，就表示优先级最低（1->2->3)
    int value() default Integer.MAX_VALUE;
}
