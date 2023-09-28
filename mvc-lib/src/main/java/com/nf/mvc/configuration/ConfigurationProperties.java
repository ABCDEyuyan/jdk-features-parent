package com.nf.mvc.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解修饰在配置属性类上，用来把yml文件中的内容赋值给配置属性类的相关属性上
 * <h3>TODO:赋值规则</h3>
 * <ul>
 *     <li>一定要靠注解指定前缀,只会读取yml中对应前缀下的内容</li>
 *     <li>如果本类的属性名在配置文件中有对应项，就进行赋值处理</li>
 * </ul>
 * @author cj
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationProperties {
    /**
     * 指定配置文件前缀用的
     * @return
     */
    String value();
}
