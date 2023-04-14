package ch03.methodref;

public class SomeClassMR {
    private String f;
    public SomeClassMR() {
        System.out.println("someClassMr默认构造函数");
    }

    public SomeClassMR(String f) {
        System.out.println("someClassMr有字符串参数的构造函数");
        this.f = f;
        System.out.println("字段赋值了");
    }

    public static void doSth(){
        System.out.println("无参无返回值");
    }



    public  void doSth2(String a){
        System.out.println("hashcode in doSth2:" + this.hashCode());
        System.out.println("---"+ a);
    }

    public static void doSth3(SomeClassMR s,String a){
        System.out.println("---"+ a);
    }
    @Override
    public String toString() {
        return "SomeClassMR{" +
                "f='" + f + '\'' +
                '}';
    }
}
