package com.nf.service;

/**
 * @ClassName Constant
 * @Author ZL
 * @Date 2023/5/11 23:12
 * @Version 1.0
 * @Explain
 **/
public class Constant {
    private Constant() {
    }

    private static final String PICTURE_LOCAL_ADDRESS="D:\\mavenproject\\zl-dbutils-mvc-task\\web\\image\\";
    public static String getLocalAddress(){
        return PICTURE_LOCAL_ADDRESS;
    }
}
