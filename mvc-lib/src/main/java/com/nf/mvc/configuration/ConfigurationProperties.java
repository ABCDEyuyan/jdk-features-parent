package com.nf.mvc.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationProperties {
    /**
     * 指定配置文件前缀用的
     * @return
     */
    String value();
}
