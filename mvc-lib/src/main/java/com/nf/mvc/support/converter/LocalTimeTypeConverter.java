package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeTypeConverter implements WebTypeConverter<LocalTime> {

    @Override
    public LocalTime convert(String value) throws Exception {
        return LocalTime.parse(value, DateTimeFormatter.ofPattern(DateTypeConverter.TIME_PATTERN));
    }
}
