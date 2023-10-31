package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

public class IntegerTypeConverter implements WebTypeConverter<Integer> {
    @Override
    public Integer convert(String value) throws Exception {
        return Integer.valueOf(value);
    }
}
