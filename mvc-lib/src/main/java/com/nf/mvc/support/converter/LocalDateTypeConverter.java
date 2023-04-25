package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class LocalDateTypeConverter implements WebTypeConverter{

    @Override
    public LocalDate convert(String paramValue) throws Exception {
        return LocalDate.parse(paramValue, DateTimeFormatter.ofPattern(DateTypeConverter.DATEPATTERN));
    }
}
