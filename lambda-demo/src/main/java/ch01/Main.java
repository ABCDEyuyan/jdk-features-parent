package ch01;

public class Main {
    public static void main(String[] args) {
        //用一个普通的实现类来赋值。
        Inf1 inf1 =  new MyInfImpl();
        inf1.doSth();

        //匿名内部类的方式来赋值
        Inf1 inf2 = new Inf1() {
            @Override
            public void doSth() {
                System.out.println("my impl2");
            }

            @Override
            public void doSth2() {

            }
        };

        inf2.doSth();
        //上面2种方法不管接口中的方法有多少个都可以
        SomeClass someClass = new SomeClass();
        someClass.doSth(new Inf1() {
            @Override
            public void doSth() {
                System.out.println("方法参数匿名内部类");
            }

            @Override
            public void doSth2() {

            }
        });
        

    }
}
