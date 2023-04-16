package ch01;

public class Main {
    public static void main(String[] args) {
        //用一个普通的实现类来赋值。
//        Inf1 inf1 =  new MyInfImpl();
//        inf1.doSth();
//        inf1.doSth2(3);

//        //匿名内部类的方式来赋值
//        Inf1 inf2 = new Inf1() {
//            @Override
//            public void doSth() {
//                System.out.println("my impl2");
//            }
//
//            @Override
//            public void doSth2(int a) {
//                a=3;
//                System.out.println(a+1);
//            }
//        };
//        inf2.doSth();
//        inf2.doSth2(1);
//        //上面2种方法不管接口中的方法有多少个都可以

        SomeClass someClass = new SomeClass();
        someClass.doSth(new Inf1() {
            @Override
            public void doSth() {
                System.out.println("方法参数匿名内部类");
            }

            @Override
            public void doSth2(int a) {
                a=1;
                System.out.println(a);
            }

            @Override
            public int doSth3(int a) {
                a=2;
                return a;
            }
        });
        MyInfImpl m=new MyInfImpl();
        m.doSth3(3);

    }
}
