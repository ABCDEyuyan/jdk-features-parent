package ch05;

/**
 * 本章讲的是类型变量边界的问题
 */
public class Main {
    public static void main(String[] args) {
        //报错，因为String不是Number的子类
        //SomeClass2<String> s = new SomeClass2<>();
       bounded();
    }

    private static void bounded() {
        SomeClass2<Number> sc2 = new SomeClass2<>();

        SomeClass3 class3 = new SomeClass3();
        Integer max = class3.max(10, 5);
        System.out.println(max);

        Student s1 = new Student(1, "a", 175);
        Student s2 = new Student(2, "b", 180);
        Student student = class3.max(s1, s2);
        System.out.println("student = " + student);
    }
}
