package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeConverter implements WebTypeConverter<LocalDateTime> {

    @Override
    public LocalDateTime convert(String value) throws Exception {
        return LocalDateTime.parse(value, DateTimeFormatter.ofPattern(DateTypeConverter.DATETIME_PATTERN));
    }
}
