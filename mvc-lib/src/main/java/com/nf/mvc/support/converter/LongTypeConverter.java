package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

public class LongTypeConverter implements WebTypeConverter<Long> {
    @Override
    public Long convert(String value) throws Exception {
        return Long.valueOf(value);
    }
}
