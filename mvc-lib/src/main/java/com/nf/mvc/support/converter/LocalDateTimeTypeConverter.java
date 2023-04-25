package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeConverter implements WebTypeConverter<LocalDateTime> {

    @Override
    public boolean supports(Class<LocalDateTime> paramType) {
        return paramType==LocalDateTime.class;
    }

    @Override
    public LocalDateTime convert(String paramValue) throws Exception {
        return LocalDateTime.parse(paramValue, DateTimeFormatter.ofPattern(DateTypeConverter.DATETIMEPATTERN));
    }
}
