package com.nf.mvc.argument;

public class SomeController2 {
    public void m1(Emp2 emp222) {
        System.out.println("=========m1 in SomeController2=======");
        System.out.println("emp222 = " + emp222);
    }


    public static void m2(Emp2 emp) {
        System.out.println("=========m2 in SomeController2=======");
        System.out.println("emp222 = " + emp);
    }
}
