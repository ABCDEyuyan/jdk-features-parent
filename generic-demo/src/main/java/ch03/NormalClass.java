package ch03;

public class NormalClass {


    public <T> T m(T u){
        return u;
    }

    public static <T> T sm(T u){
        System.out.println("sm----");
        return u;
    }
}
