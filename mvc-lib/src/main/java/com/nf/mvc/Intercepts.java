package com.nf.mvc;

import com.nf.mvc.support.PathMatcher;
import com.nf.mvc.support.path.AntPathMatcher;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 此注解修饰在拦截器上，用来指定要拦截的地址与不进行拦截的地址，
 * 默认的HandlerMapping实现类{@link com.nf.mvc.mapping.RequestMappingHandlerMapping}
 * 对于拦截器地址的解析是采用ant地址模式进行解析的
 *
 * <p>通过value属性指定要拦截的地址，默认值是拦截所有的地址<br/>
 * 通过excludePattern属性指定不拦截的地址，默认值是空，意思就是没有要排除的地址
 * </p>
 *
 * <p>如果includePattern与excludePattern设置有冲突以排除设置为准</p>
 *
 * @see com.nf.mvc.HandlerInterceptor
 * @see AntPathMatcher
 * @see com.nf.mvc.mapping.RequestMappingHandlerMapping
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Intercepts {
    String[] value() default {"/**"};

    String[] excludePattern() default {""};

    /**
     * 此属性用来设定拦截器注解对要进行拦截的路径模式与要排除的路径模式的路径匹配器
     * <p>通常情况下，{@link HandlerMapping}与注解{@link Intercepts}用到的路径模式匹配器是一致的，
     * {@link com.nf.mvc.mapping.RequestMappingHandlerMapping}就是这样做的。
     * 但你也可以让它们两者用到的路径模式匹配器不一致，那么就可以用此属性来指定拦截器注解用到的路径模式匹配器。
     * 甚至此属性的值你可以完全不用理会，完全用自己的模式匹配器去处理includePattern与excludePattern，
     * 甚至路径模式匹配器不是一个PathMatcher都可以，这点通常由{@link HandlerMapping}的实现类去决定
     * </p>
     *
     * @return PathMatcher的某个实现类
     */
    Class<? extends PathMatcher> pathMatcher() default AntPathMatcher.class;
}
