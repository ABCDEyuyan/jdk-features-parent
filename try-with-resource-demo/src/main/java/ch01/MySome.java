package ch01;

//Closeable接口是AutoCloseable接口的子接口
public class MySome implements AutoCloseable{
    @Override
    public void close() throws Exception {
        System.out.println("----close-");
    }

    public void doSth(){
        System.out.println("do sth****");
    }
}
