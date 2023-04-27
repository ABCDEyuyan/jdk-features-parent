package com.nf.mvc;

import java.util.List;

public class CorsConfiguration {
    public static final String ALL = "*";

    public static final String ORIGIN = "Origin";

    public static final String REFERER = "Referer";

    public static final String TRUE = "true";
    public static final String FALSE = "false";

    public static final String CACHE_86400 = "86400";

    public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

    public static final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";

    public static final String ACCESS_CONTROL_REQUEST_METHOD = "Access-Control-Request-Method";

    public static final String ACCESS_CONTROL_REQUEST_HEADERS = "Access-Control-Request-Headers";

    public static final String ACCESS_CONTROL_MAX_AGE = "Access-Control-Max-Age";

    public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

    public static final String ACCESS_CONTROL_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public void setAllowedOrigins(String... origins) {
    }

    public List<String> getAllowedOrigins() {
        return null;
    }

    public void setAllowCredentials(Boolean allowCredentials) {

    }

    public void setAllowedHeaders(String... allowedHeaders) {

    }

    public boolean getAllowedHeaders() {
        return false;
    }

    public void setAllowedMethods(String... allowedMethods) {

    }

    public List<String> getAllowedMethods() {
        return null;
    }

}
