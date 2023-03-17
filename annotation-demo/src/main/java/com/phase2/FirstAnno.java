package com.phase2;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

//@Target({ElementType.METHOD}) //修饰注解的这个Target是元注解，用来设定注解可以用在哪里
@Target({ElementType.METHOD,
        ElementType.TYPE,
        ElementType.FIELD,
        ElementType.PARAMETER})
//修饰注解的这个Target是元注解，用来设定注解可以用在哪里
public @interface FirstAnno {
}
