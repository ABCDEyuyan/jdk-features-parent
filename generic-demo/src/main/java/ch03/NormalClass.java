package ch03;

public class NormalClass {


    public <T> T m(T u){
        return u;
    }

    public static <T> T sm(T u){
        System.out.println("sm----");
        return u;
    }

    public static <T> T sm2(String a){
        System.out.println("sm2----");
        return null;
    }
}
