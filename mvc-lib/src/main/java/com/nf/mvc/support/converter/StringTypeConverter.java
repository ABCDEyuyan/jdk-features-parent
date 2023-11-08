package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

public class StringTypeConverter implements WebTypeConverter<String> {

    @Override
    public String convert(String value) throws Exception {
        return value;
    }
}
