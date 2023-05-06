package mvcdemo.controller;

import com.nf.mvc.HandlerMapping;
import com.nf.mvc.WebMvcConfigurer;
import com.nf.mvc.mapping.RequestMappingHandlerMapping;
import com.nf.mvc.support.EqualIgnoreCasePathMatcher;
import com.nf.mvc.support.EqualPathMatcher;

public class MyConfigurer implements WebMvcConfigurer {
    @Override
    public void configureHandlerMapping(HandlerMapping handlerMapping) {
        if (handlerMapping instanceof RequestMappingHandlerMapping) {
            RequestMappingHandlerMapping mapping = (RequestMappingHandlerMapping) handlerMapping;
            mapping.setPathMatcher(new EqualIgnoreCasePathMatcher());
        }

    }
}
