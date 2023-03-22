package com.nf;

public class UserExistException extends RuntimeException{
    //下面3个是基础的写法
    //1. 默认构造函数：表示只是有个错误，无特别的信息
    public UserExistException() {
        super();
    }

    //2.一个字符串类型的构造函数，表示有异常的描述信息
    public UserExistException(String message) {
        super(message);
    }

    //3.有异常的描述信息，并且还包括上一个异常信息
    public UserExistException(String message, Throwable cause) {
        super(message, cause);
    }
    //在异常类里面是可以写额外的代码，实际项目中有这种情况

}
