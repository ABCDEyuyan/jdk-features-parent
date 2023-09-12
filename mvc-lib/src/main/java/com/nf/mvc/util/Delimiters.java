package com.nf.mvc.util;

import javax.servlet.ServletConfig;
import java.util.EnumSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 此枚举类编写了常见分隔符的正则表达式，方便使用。
 * <p>枚举成员通常用作字符串拆分的分隔符使用，可以单独使用这些分隔符，
 * <pre class="code">
 *      List<String> stringList = StringUtils.split(data, Delimiters.Colon.getPattern());
 * </pre>
 * 也可以组合使用:
 * <pre class="code">
 *      String combinedPattern = Delimiters.getCombinedPattern(EnumSet.of( Delimiters.Colon,Delimiters.Common));
 *      List<String> stringList = StringUtils.split(data, combinedPattern);
 * </pre>
 * 组合使用是一种或者的关系，意味着字符串中只要出现了组合的模式中的任意成员都会进行拆分操作
 * </p>
 * <p>具体的使用地方见{@link com.nf.mvc.DispatcherServlet#getBasePackages(ServletConfig)}</p>
 * @see StringUtils
 * @see com.nf.mvc.DispatcherServlet
 */
public enum Delimiters {
    Comma(",+","逗号分隔符"),
    Space("\\s+","空格分隔符"),
    SemiColon(";+","分号分隔符"),
    Common("[\\s,;]+","spring常用的分隔符，逗号，空格，分号"),
    Hyphen("-+","连字符分隔符"),
    Colon(":+","冒号")
    ;
    private String pattern;
    private String desc;

    Delimiters(String pattern, String desc) {
        this.pattern = pattern;
        this.desc = desc;
    }

    public String getPattern() {
        return pattern;
    }

    public String getDesc() {
        return desc;
    }

    /**
     * TODO:补上Delimiters EnumSet的使用方法
     * @param delimiters
     * @return
     */
    public static String getCombinedPattern(EnumSet<Delimiters> delimiters) {
        List<String> patternList = delimiters.stream().map(Delimiters::getPattern).collect(Collectors.toList());
        return String.join("|",patternList);
    }
}
