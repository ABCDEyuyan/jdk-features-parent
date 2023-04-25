package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

public class LongTypeConverter implements WebTypeConverter<Long> {

    @Override
    public boolean supports(Class<Long> paramType) {
        return paramType==Long.class || paramType==Long.TYPE;
    }

    @Override
    public Long convert(String paramValue)throws Exception {
        return Long.valueOf(paramValue);
    }
}
