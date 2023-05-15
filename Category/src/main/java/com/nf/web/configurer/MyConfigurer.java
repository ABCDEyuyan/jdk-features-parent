package com.nf.web.configurer;

import com.nf.mvc.CorsConfiguration;
import com.nf.mvc.HandlerMapping;
import com.nf.mvc.WebMvcConfigurer;

public class MyConfigurer implements WebMvcConfigurer {
    @Override
    public void configureHandlerMapping(HandlerMapping handlerMapping) {
    /*if (handlerMapping instanceof RequestMappingHandlerMapping) {
        RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) handlerMapping;
        mapping.setPathMatcher(new EqualIgnoreCasePathMatcher());
    }*/

    }

    @Override
    public void configureCors(CorsConfiguration configuration) {
        configuration.setAllowedOrigins("http://localhost:5500").setAllowedOrigins("http://127.0.0.1:5500");
    }
}
