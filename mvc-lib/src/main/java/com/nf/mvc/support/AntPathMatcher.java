package com.nf.mvc.support;


import com.nf.mvc.util.StringUtils;

import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.nf.mvc.util.StringUtils.skipBlanks;

/** 此类参考spring 的AntPathMatcher实现.
 * <p>ant地址主要用在web的url路径匹配上,其中三个核心通配符含义如下</p>
 * <ul>
 *   <li>'?' - 匹配一个字符</li>
 *   <li>'*' - 匹配0个或多个字符</li>
 *   <li>'**' - 在一个路径中匹配0个或多个部分(directory)</li>
 * </ul>
 * <h3>核心特性</h3>
 * 在此类的实现中,有4个选项是可以调整的,可以利用{@link AntPathMatcher.Builder}的对应方法进行调整
 * <ul>
 *   <li>pathSeparator:路径分隔符,默认值是"/"</li>
 *   <li>ignoreCase:忽略大小写,默认值是false</li>
 *   <li>trimTokens:删除前后空白,默认是false</li>
 *   <li>matchStart:模式前面部分匹配路径就算匹配,默认值是false</li>
 * </ul>
 * 关于matchStart的含义,下面一个示例代码解释了其具体含义,其中pathMatcher2设置matchStart为true,
 * <i>其中pattern变量的* 符号左右是没有空格的,这里加上空格是为了在文档化注释中显示正确.</i>
 * 可以看到当设置matchStart位true之后,整个路径只要与pattern对应的前面部分匹配,模式即使多了一些内容,
 * 比如这里的d这一部分,也算是匹配
 * <pre class="code">
 *         AntPathMatcher pathMatcher = new AntPathMatcher.Builder().build();
 *         String pattern = "a/ * /c/d";
 *         String path = "a/b0/c";
 *
 *         boolean match = pathMatcher.isMatch(pattern, path);//false
 *         System.out.println("match = " + match);
 *
 *         AntPathMatcher pathMatcher2 = new AntPathMatcher.Builder().withMatchStart().build();
 *         boolean matchStart = pathMatcher2.isMatch(pattern, path); //true
 *         System.out.println("matchStart = " + matchStart);
 * </pre>
 * <h3>参考资料</h3>
 * <p>参考spring的PathMatcher，AntPathMatcher。现在spring 5.0有另一个新的路径处理的类PathPattern（spring 5.0才出现）</p>
 * <a href="https://github.com/azagniotov/ant-style-path-matcher">AntPathMatcher简单实现</a>
 * <a href="https://my.oschina.net/iqoFil/blog/221623">spring AntPathMatcher匹配算法解析</a>
 * @author cj
 */
public class AntPathMatcher implements PathMatcher{
    private static final char ASTERISK = '*';
    private static final char QUESTION = '?';
    /**
     * 路径变量写法的正则表达式,比如{pageSize}这种写法就匹配这个正则表达式
     */
    private static final String PATH_VARIABLE_PATTERN = "\\{.*?\\}";
    private static final int ASCII_CASE_DIFFERENCE_VALUE = 32;
    private static final int LENGTH_OF_TWO_PATTERN_CHAR = 2;

    private final char pathSeparator;
    private final boolean ignoreCase;
    private final boolean matchStart;
    private final boolean trimTokens;

    private AntPathMatcher(final char pathSeparator, boolean ignoreCase, boolean matchStart, boolean trimTokens) {
        this.pathSeparator = pathSeparator;
        this.ignoreCase = ignoreCase;
        this.matchStart = matchStart;
        this.trimTokens = trimTokens;
    }
    @Override
    public boolean isMatch( String pattern, String path) {
        //这个if块就是用来把路径变量替换为一个*来处理匹配问题的
        if (StringUtils.hasText(pattern)) {
            pattern = pattern.replaceAll(PATH_VARIABLE_PATTERN, "*");
        }
        if (pattern.isEmpty()) {
            return path.isEmpty();
        } else if (path.isEmpty() && pattern.charAt(0) == pathSeparator) {
            if (matchStart) {
                return true;
            } else if (pattern.length() == LENGTH_OF_TWO_PATTERN_CHAR && pattern.charAt(1) == ASTERISK) {
                return false;
            }
            return isMatch(pattern.substring(1), path);
        }

        final char patternStart = pattern.charAt(0);
        if (patternStart == ASTERISK) {
            if (pattern.length() == 1) {
                return path.isEmpty() || path.charAt(0) != pathSeparator && isMatch(pattern, path.substring(1));
            } else if (doubleAsteriskMatch(pattern, path)) {
                return true;
            }

            int start = 0;
            while (start < path.length()) {
                if (isMatch(pattern.substring(1), path.substring(start))) {
                    return true;
                }
                start++;
            }
            return isMatch(pattern.substring(1), path.substring(start));
        }

        int pointer = trimTokens==true?skipBlanks(path):0;

        return !path.isEmpty() && (equal(path.charAt(pointer), patternStart) || patternStart == QUESTION)
                && isMatch(pattern.substring(1), path.substring(pointer + 1));
    }

    public Map<String, String> extractPathVariables(String pattern, String path) {
        Map<String,String> pathVariables = new LinkedHashMap<>();
        if (isMatch(pattern, path)) {
            String[] patternSegments = StringUtils.tokenizeToStringArray(pattern, ""+this.pathSeparator,this.trimTokens,true);
            String[] pathSegments = StringUtils.tokenizeToStringArray(path, ""+this.pathSeparator,this.trimTokens,true);
            for (int i=0; i< pathSegments.length;i++) {
                String patternSegment = patternSegments[i];
                if (patternSegment.startsWith("{") && patternSegment.endsWith("}")) {
                    String variableName = patternSegment.substring(1,patternSegment.length()-1);
                    String variableValue = pathSegments[i];
                    pathVariables.put(variableName, variableValue);
                }
            }
        }
        return pathVariables;
    }
    private boolean doubleAsteriskMatch(final String pattern, final String path) {
        if (pattern.charAt(1) != ASTERISK) {
            return false;
        } else if (pattern.length() > LENGTH_OF_TWO_PATTERN_CHAR) {
            return isMatch(pattern.substring(3), path);
        }
        return false;
    }

    @Override
    public Comparator<String> getPatternComparator(String path) {
        return new AntPatternComparator(path);
    }

    private boolean equal(final char pathChar, final char patternChar) {
        if (ignoreCase) {
            return pathChar == patternChar ||
                    ((pathChar > patternChar) ?
                            pathChar == patternChar + ASCII_CASE_DIFFERENCE_VALUE :
                            pathChar == patternChar - ASCII_CASE_DIFFERENCE_VALUE);
        }
        return pathChar == patternChar;
    }

    public static final class Builder {

        private char pathSeparator = '/';
        private boolean ignoreCase = false;
        private boolean matchStart = false;
        private boolean trimTokens = false;

        public Builder() {

        }

        public Builder withPathSeparator(final char pathSeparator) {
            this.pathSeparator = pathSeparator;
            return this;
        }

        public Builder withIgnoreCase() {
            this.ignoreCase = true;
            return this;
        }

        public Builder withMatchStart() {
            this.matchStart = true;
            return this;
        }

        public Builder withTrimTokens() {
            this.trimTokens = true;
            return this;
        }

        public AntPathMatcher build() {
            return new AntPathMatcher(pathSeparator, ignoreCase, matchStart, trimTokens);
        }
    }
}