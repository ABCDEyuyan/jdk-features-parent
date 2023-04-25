package com.nf.mvc.support.converter;


import com.nf.mvc.support.WebTypeConverter;

public class CharacterTypeConverter implements WebTypeConverter<Character> {

    @Override
    public boolean supports(Class<Character> paramType) {
        return paramType==Character.class || paramType==Character.TYPE;
    }

    @Override
    public Character convert(String paramValue) throws Exception {
        return Character.valueOf(paramValue.charAt(0));
    }
}
