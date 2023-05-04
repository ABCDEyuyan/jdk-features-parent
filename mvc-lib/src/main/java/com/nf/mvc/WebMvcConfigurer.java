package com.nf.mvc;

import com.nf.mvc.adapter.RequestHandlerAdapterConfigurer;
import com.nf.mvc.mapping.RequestMappingConfigurer;

/**
 * 此接口是用来给mvc框架内部的一些核心组件提供配置使用的
 * 这里只写了对RequestMappingHandlerMapping与CorsConfiguration组件的配置能力
 * 你可以多提供一些方法来对其它组件进行配置，比如ArgumentResolver
 */
public interface WebMvcConfigurer {
    default void configureRequestHandlerMapping(RequestMappingConfigurer configurer) {

    }

    default void configureCors(CorsConfiguration configuration) {

    }

}
