package com.nf.mvc.support.converter;


import com.nf.mvc.support.WebTypeConverter;

public class BooleanTypeConverter implements WebTypeConverter<Boolean> {
    @Override
    public Boolean convert(String value) throws Exception {
        return Boolean.valueOf(value);
    }
}
