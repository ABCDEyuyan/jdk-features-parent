package ch06;

/**
 * 类型擦除的问题
 * java泛型是一个编译时的技术，编译之后，为了向后兼容，
 * 编译器会把泛型相关的信息擦除掉，那么就可以让老的jvm正常执行java代码
 * @param <T>
 */
public class MyParent<T> {
    public T data;
    public void setData(T data) {
        System.out.println("---parent ---");
        this.data  = data;
    }
}

//经过擦除之后
//public class MyParent {
//    public Object data;
//    public void setData(Object data) {
//        System.out.println("---parent ---");
//        this.data  = data;
//    }
//}