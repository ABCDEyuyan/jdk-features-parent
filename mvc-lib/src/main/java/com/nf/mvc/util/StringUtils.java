package com.nf.mvc.util;

import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

/**
 * 此类拷贝自spring的StringUtils类
 */
public abstract class StringUtils {
    private static final String[] EMPTY_STRING_ARRAY = {};

    private static final String FOLDER_SEPARATOR = "/";

    private static final String WINDOWS_FOLDER_SEPARATOR = "\\";

    private static final String TOP_PATH = "..";

    private static final String CURRENT_PATH = ".";

    private static final char EXTENSION_SEPARATOR = '.';

    /**
     * 把List字符串数据变成一个分隔符分隔的字符串，比如List集合中有a b c三条记录，<br/>
     * 而指定的分隔符是逗号，那么调用此方法之后生成的字符串是：a,b,c
     * @param stringList
     * @param delimiter
     * @return
     */
    public static String toCommaDelimitedString(List<String> stringList,String delimiter) {
        StringJoiner joiner = new StringJoiner(delimiter);
        for (String val : stringList) {
            if (val != null) {
                joiner.add(val);
            }
        }
        return joiner.toString();
    }

    public static boolean hasLength(CharSequence str) {
        return (str != null && str.length() > 0);
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

    public static boolean containsWhitespace(CharSequence str) {
        if (!hasLength(str)) {
            return false;
        }

        int strLen = str.length();
        for (int i = 0; i < strLen; i++) {
            if (Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

    public static boolean containsWhitespace(String str) {
        return containsWhitespace((CharSequence) str);
    }

    /**
     * 裁剪掉所有的空白字符，包含前部，中间，尾部的所有空白
     * @param str
     * @return
     */
    public static String trimAllWhitespace(String str) {
        if (!hasLength(str)) {
            return str;
        }

        int len = str.length();
        StringBuilder sb = new StringBuilder(str.length());
        for (int i = 0; i < len; i++) {
            char c = str.charAt(i);
            if (!Character.isWhitespace(c)) {
                sb.append(c);
            }
        }
        return sb.toString();
    }

    /**
     * 利用String自带的split方法进行字符串拆分，拆分前会调用trim方法剔除掉前后所有的空白，
     * split的拆分方法可能导致前部出现空的字符串，比如用逗号作为分隔符拆分",,,abc,def"这样的数据，
     * 本方法剔除了拆分为长度为0的空字符串
     * @param source:一个字符串数据
     * @param regex：正则表达式，通常是分隔符，可以使用{@link Delimiters}来指定常用的分隔符及分隔符组合
     * @return
     */
    public static List<String> split(String source,String regex) {
        Assert.notNull(source,"原始字符串不能是null的");
        String[] strings = source.trim().split(regex);
        return Arrays.stream(strings).filter(s -> s.length() != 0).collect(Collectors.toList());
    }
}
