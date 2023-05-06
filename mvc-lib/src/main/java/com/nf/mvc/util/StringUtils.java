package com.nf.mvc.util;

import java.util.List;
import java.util.StringJoiner;

public abstract class StringUtils {
    public static String toCommaDelimitedString(List<String> stringList,String delimiter) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (String val : stringList) {
            if (val != null) {
                joiner.add(val);
            }
        }
        return joiner.toString();
    }

    public static boolean hasText( String str) {
        return (str != null && !str.isEmpty() && containsText(str));
    }

    private static boolean containsText(CharSequence str) {
        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }
}
