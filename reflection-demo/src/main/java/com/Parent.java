package com;

public class Parent {

    //字段（Field）
    private String name;
    private int id;

    //默认构造函数（Constructor）
    public Parent() {
        System.out.println("---无参的默认构造函数");
    }

    //构造函数（带一个参数（Parameter）
    public Parent(String name) {
        System.out.println("---name的构造函数:" +name);
        this.name = name;
    }

    //方法（Method）
    //每一个方法由下面几个部分组成
    // 1. 名字
    //2. 修饰符
    //3. 返回类型
    //4.参数，数组（个数与类型）
    public void m1(){
        System.out.println("m1 in parent---");
    }
    public void m1(String name){
        System.out.println("m1 in parent---");
    }

    public void m2(){
        System.out.println("m2 in parent---");
    }

    public void m3(int id){
        System.out.println("m2 in parent---");
    }

    @Override
    public String toString() {
        return "this is to string -----";
    }

    public int getId() {
        return id;
    }
}
