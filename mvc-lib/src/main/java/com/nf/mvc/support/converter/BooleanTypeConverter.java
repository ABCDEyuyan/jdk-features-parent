package com.nf.mvc.support.converter;


import com.nf.mvc.support.WebTypeConverter;

public class BooleanTypeConverter implements WebTypeConverter<Boolean> {

    @Override
    public boolean supports(Class<Boolean> paramType) {
        return paramType==Boolean.class || paramType==Boolean.TYPE;
    }

    @Override
    public Boolean convert(String paramValue) throws Exception {
        return Boolean.valueOf(paramValue);
    }
}
