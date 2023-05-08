package com.nf.mvc.support;

import java.util.Comparator;

public interface PathMatcher {
    /**
     * @param pattern:模式，比如/*,
     * @param path:具体的请求地址/a
     * @return
     */
    boolean isMatch(final String pattern, final String path);

    default Comparator<String> getPatternComparator(String path){
        throw new UnsupportedOperationException("不支持对模式进行比较");
    }
}
