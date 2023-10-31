package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public class LocalDateTypeConverter implements WebTypeConverter<LocalDate> {

    @Override
    public LocalDate convert(String value) throws Exception {
        return LocalDate.parse(value, DateTimeFormatter.ofPattern(DateTypeConverter.DATE_PATTERN));
    }
}
