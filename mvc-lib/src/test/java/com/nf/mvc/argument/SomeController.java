package com.nf.mvc.argument;

import java.util.List;

public class SomeController {
    public void m1(int id,String name) {
        System.out.println("=========m1 in SomeController=======");
        System.out.println("id = " + id);
        System.out.println("name = " + name);
    }

    public void m2(List<String > list) {
        System.out.println("=========m2 in SomeController=======");

    }
}
