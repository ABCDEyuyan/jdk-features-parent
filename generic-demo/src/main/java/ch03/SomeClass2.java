package ch03;

public class SomeClass2 <T>{
    //不要这样写，因为有歧义：方法上的T与类上的T到底是什么关系
   /* public <T> void m(){

    }*/

    public <U> void m(){

    }


    public <U> void m2(T t,U u){

    }
}
