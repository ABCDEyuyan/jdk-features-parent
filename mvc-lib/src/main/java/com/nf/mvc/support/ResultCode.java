package com.nf.mvc.support;

/**
 * 此接口是用来记录常用的响应状态码与对应的响应消息的，推荐使用枚举来实现此接口，
 * 在mvc库里实现了一个常用状态码的接口，你可以实现一个跟你业务相关的此接口的实现类，
 * 以便于ResponseVO类配合使用
 * @see ResponseVO
 * @see CommonResultCode
 */
public interface ResultCode {

    Integer getCode();
    String getMessage();
}
