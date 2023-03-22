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

    /*    try (MyCloseable my = new MyCloseable();
             String sql="sadfasd";
            MyCloseable2 my2 = new MyCloseable2()) {


        }*/

        sm2();
    }

    private static void sm1(){
        try {
            new C().m1();
        } finally {
            throw new RuntimeException("finally");
        }
    }

    private static void sm2(){
        try {
            sm1();
        } catch (RuntimeException re){
            System.out.println(re.getMessage());
        }
    }
}
