package com.phase3;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 注解里面声明成员都是方法形式
 * 方法不能有参数
 * 成员的返回类型是有限定，只能是以下类型及这些类型的数组类型
 *  1.所有的基本类型（int,float,boolean,byte,double,char,long,short)
 *  2.String
 *  3.Class
 *  4.enum
 *  5 Annotation(注解）
 *
 */
@Target({ElementType.METHOD})
public @interface ThirdAnno {


    int aa();
   // String bb();
    //B bb();
}
