package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

public class StringTypeConverter implements WebTypeConverter<String> {

    @Override
    public boolean supports(Class<String> paramType) {
        return paramType==String.class;
    }

    @Override
    public String convert(String paramValue) throws Exception {
        return paramValue;
    }
}
