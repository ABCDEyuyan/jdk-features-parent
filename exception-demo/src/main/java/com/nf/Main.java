package com.nf;

import java.sql.Connection;
import java.sql.DriverManager;

public class Main {
    public static void main(String[] args) throws Exception {

     /*   try {
            new B().m1();
        } catch (RuntimeException re) {
            re.printStackTrace();
        }*/


      /*  try {
            new A().m1();
        } catch (RuntimeException re) {
            re.printStackTrace();
        }*/

        try (MyCloseable my = new MyCloseable();
            MyCloseable2 my2 = new MyCloseable2()) {
            my2.m1();
            my.m1();

        }
    }

}
