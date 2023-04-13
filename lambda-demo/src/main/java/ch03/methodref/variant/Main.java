package ch03.methodref.variant;

/**
 * 委托支持协变和逆变，为匹配委托类型和方法签名提供更大的灵活性，
 * 不仅可以将签名完全匹配的方法分配给委托实例，
 * 还可以通过协变将返回值类型与委托类型的返回值类型相比派生程度更大的方法分配给委托实例；
 * 通过逆变将参数类型与委托类型的参数类型相比派生程度更小的方法分配给委托实例：
 * https://www.cnblogs.com/minotauros/p/10090701.html
 * <p>
 * 返回值端：协变
 * 参数端：逆变
 */
public class Main {
    public static void main(String[] args) {
        // 逆变
        contravariant();
        //协变
        //covariant();
    }

    //逆变
    private static void contravariant() {
        // 当接口的类型是某个类型（B)，那么关联方法时，
        // 方法的参数可以是这个类型本身以及它的父类型
        Inf1 inf1 = SomeClass::m1; //m1参数为A
        Inf1 inf2 = SomeClass::m2; //m2参数为B
        //Inf1 inf3 = SomeClass::m3;
        inf1.doSth(new B());
        inf2.doSth(new B());
    }

    //协变
    private static void covariant() {
        //当接口的返回类型是某个类型，那么关联方法时，
        // 方法的返回类型可以是这个类型本身以及它的子类型
        // Inf2 inf1 = SomeClass2::m1;
        Inf2 inf2 = SomeClass2::m2;
        Inf2 inf3 = SomeClass2::m3;
        //A a = inf1.doSth();
        B b = inf2.doSth();
        C c = (C) inf3.doSth();
        // System.out.println(a.getClass());
        System.out.println(b.getClass());
        System.out.println(c.getClass());
    }


}
