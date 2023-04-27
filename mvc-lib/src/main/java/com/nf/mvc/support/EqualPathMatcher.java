package com.nf.mvc.support;

public class EqualPathMatcher implements PathMatcher {
    @Override
    public boolean isMatch(String pattern, String path) {
        return path.equals(pattern);
    }
}
