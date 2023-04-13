package ch03.methodref;

// 这个类与InfMr5这些类型配合理解用的，主要是在讲解关联特定类的任意实例的实例方法时使用
public class FirstParam {
    //第一个参数是自己的类型，并且形参名是this的时候，
    // 调用这样的方法不需要指定第一个参数
    public void doSth(FirstParam this, Integer a) {
        System.out.println(a);
    }

    public static void main(String[] args) {
        FirstParam firstParam = new FirstParam();
        //因为第一个参数就是firstParam变量指向的对象，所以可以不用传
        firstParam.doSth(1000);
    }
}
