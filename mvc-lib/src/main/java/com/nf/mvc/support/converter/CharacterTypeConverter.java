package com.nf.mvc.support.converter;


import com.nf.mvc.support.WebTypeConverter;

public class CharacterTypeConverter implements WebTypeConverter<Character> {
    @Override
    public Character convert(String value) throws Exception {
        return value.charAt(0);
    }
}
