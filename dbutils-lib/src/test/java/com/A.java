package com;

public class A {


    public void m1(){
        String conn = preConn();

        System.out.println(conn +" 编写cud操作");
    }

    public String preConn(){
        return "datasource.getConnection";
    }

    public static void main(String[] args) {
        A a = new A();

        a.m1();

      /*  AEx a = new AEx();
        a.m1();*/
    }
}

class AEx extends A {
    @Override
    public String preConn(){
        String result =  "datasource.getConnection";
        result += "readonly = true";
        return  result;
    }
}