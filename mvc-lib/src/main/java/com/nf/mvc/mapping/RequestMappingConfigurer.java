package com.nf.mvc.mapping;

import com.nf.mvc.support.PathMatcher;

public class RequestMappingConfigurer {
    private PathMatcher pathMatcher;
    private String name;
    public RequestMappingConfigurer pathMatcher(PathMatcher pathMatcher){
        this.pathMatcher = pathMatcher;
        return  this;
    }

    public RequestMappingConfigurer name(String name){
        this.name = name;
        return  this;
    }

    public PathMatcher getPathMatcher() {
        return pathMatcher;
    }

    public String getName() {
        return name;
    }

    public void config(RequestMappingHandlerMapping handlerMapping) {
        handlerMapping.setName(this.name);
        handlerMapping.setPathMatcher(this.pathMatcher);
    }
}
