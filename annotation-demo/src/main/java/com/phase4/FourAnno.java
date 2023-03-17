package com.phase4;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface FourAnno {
    //注解里面有一个特殊的成员名value
    //使用时可以不用指定名字
    int value();
    String aa();
}
