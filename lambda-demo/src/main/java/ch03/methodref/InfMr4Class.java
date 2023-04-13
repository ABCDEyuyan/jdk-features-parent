package ch03.methodref;

public class InfMr4Class {
    private String name ;

    public InfMr4Class(String name) {
        this.name = name;
    }

    public void doSth(String a) {

        System.out.println("name:"
                        + this.name
                        + " ---" + a);
    }

}
