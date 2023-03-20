package com.phase6;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
//本能：每次写自己的注解的时候，至少要加下面的这2个元注解
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface SixAnno {
    int value() default 100;

    String name() default "abc";
}
