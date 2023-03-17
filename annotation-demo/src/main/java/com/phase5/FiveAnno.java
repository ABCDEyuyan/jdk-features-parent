package com.phase5;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface FiveAnno {
    int value() default 100;
}
