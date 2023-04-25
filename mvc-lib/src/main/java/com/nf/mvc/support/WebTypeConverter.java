package com.nf.mvc.support;

public interface WebTypeConverter<T> {
    boolean supports(Class<T> paramType);
    T convert(String paramValue) throws Exception;

}