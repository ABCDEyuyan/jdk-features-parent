package com.nf.dbutils.util;

public class StringUtils {
    /**
     * 把一个字符串转换成驼峰命名规范的字符串，比如get_by_id转换成getById
     * @param text 要进行转换的字符串
     * @param delimiter 分隔符，通常是下划线，空格之类的字符
     * @return 驼峰规范的字符串
     * @see <a href="https://www.baeldung.com/java-string-to-camel-case#:~:text=String%20camelCase%20%3D,CaseFormat.UPPER_UNDERSCORE.to%20%28CaseFormat.LOWER_CAMEL%2C%20%22THIS_STRING_SHOULD_BE_IN_CAMEL_CASE%22%29%3B">字符串转换为驼峰</a>
     */
    public static String toCamelCase(String text, char delimiter) {
        boolean shouldConvertNextCharToLower = true;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < text.length(); i++) {
            char currentChar = text.charAt(i);
            if (currentChar == delimiter) {
                shouldConvertNextCharToLower = false;
            } else if (shouldConvertNextCharToLower) {
                builder.append(Character.toLowerCase(currentChar));
            } else {
                builder.append(Character.toUpperCase(currentChar));
                shouldConvertNextCharToLower = true;
            }
        }
        return builder.toString();
    }
}
