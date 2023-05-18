package ch06;

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