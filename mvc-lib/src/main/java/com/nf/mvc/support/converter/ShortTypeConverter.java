package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

public class ShortTypeConverter implements WebTypeConverter<Short> {

    @Override
    public Short convert(String value) throws Exception {
        return Short.valueOf(value);
    }
}
