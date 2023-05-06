package com.nf.mvc.support;

public interface PathMatcher {
    /**
     *
     * @param pattern:模式，比如/*,
     * @param path:具体的请求地址/a
     * @return
     */
    boolean isMatch(final String pattern, final String path);
}
