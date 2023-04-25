package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

public class ShortTypeConverter implements WebTypeConverter<Short> {

    @Override
    public boolean supports(Class<Short> paramType) {
        return paramType==Short.class || paramType==Short.TYPE;
    }

    @Override
    public Short convert(String paramValue) throws Exception {
        return Short.valueOf(paramValue);
    }
}
