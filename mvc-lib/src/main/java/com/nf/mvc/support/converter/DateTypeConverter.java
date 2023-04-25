package com.nf.mvc.support.converter;

import com.nf.mvc.support.WebTypeConverter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeConverter implements WebTypeConverter<Date> {

    public static final String DATEPATTERN = "yyyy-MM-dd";
    public static final String TIMEPATTERN = "HH:mm:ss";
    public static final String DATETIMEPATTERN = "yyyy-MM-dd HH:mm:ss";

    @Override
    public boolean supports(Class<Date> paramType) {
        return paramType==Date.class;
    }

    @Override
    public Date convert(String paramValue) throws Exception {
        try {
            return new SimpleDateFormat(DATETIMEPATTERN).parse(paramValue);
        } catch (ParseException e1) {
            throw new IllegalArgumentException("Date convert error.", e1);
        }
    }
}
