package ch06;

import java.lang.reflect.Method;

public class Main {
    public static void main(String[] args) {
        Method[] declaredMethods = MyChild.class.getDeclaredMethods();


        for (Method method : declaredMethods) {
            System.out.println(method.getName());
            System.out.println(method.getParameterTypes()[0]);
            System.out.println(method.isBridge());//是否是桥接方法
            System.out.println("-----------");
        }
    }
}
