package com;

public class Parent {

    //字段（Field）
    private String name;
    private int id;

    //默认构造函数（Constructor）
    public Parent() {
    }

    //构造函数（带一个参数（Parameter）
    public Parent(String name) {
        this.name = name;
    }

    //方法（Method）
    public void m1(){
        System.out.println("m1 in parent---");
    }

    public void m2(){
        System.out.println("m2 in parent---");
    }

    public void m3(int id){
        System.out.println("m2 in parent---");
    }
}
