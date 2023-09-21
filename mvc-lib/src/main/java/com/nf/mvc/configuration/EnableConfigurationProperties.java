package com.nf.mvc.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 暂定此注解只能修饰在入口函数main所在的类上面，用来指定哪些类型是一个配置属性类，
 * 可以同时指定多个配置属性类
 * <h3>典型用法</h3>
 * <ul>
 *     <li>在入口类上添加注解{@link EnableConfigurationProperties}指定有哪些配置属性类</li>
 *     <li>编写配置属性类，并在此类上指定注解{@link ConfigurationProperties},并指定前缀</li>
 *     <li>在要使用配置属性的类中，声明一个类型为配置属性类的字段，并在其上添加注解{@link com.nf.mvc.ioc.Autowired}</li>
 *     <li>有了这个字段后，就可以调用其属性来获取从application.yml中获取的配置了</li>
 * </ul>
 * 示例代码如下:
 * <pre class="code">
 *
 * &#064;EnableConfigurationProperties({MyConfigurationProperties})
 * public class MyApplication{
 *     public static void main(String[] args) {
 *
 *     }
 * }
 * &#064;ConfigurationProperties("server")
 * public class MyConfigurationProperties{
 *     private int port;
 *     private String path;
 *     //getter setter...
 * }
 *
 * public class SomeClass {
 *     &#064;Autowired
 *     private MyConfigurationProperties config;
 *     public void doSth(){
 *         int port = config.getPort();
 *         String path = config.getPath();
 *     }
 * }
 * yml配置如下:
 * server:
 *      port: 8000
 *      path: /somePath
 *
 * </pre>
 * <h3>配置属性类</h3>
 * 配置属性类是修饰了{@link ConfigurationProperties}注解的类型，此类型都是一些简单的属性，
 * 这些属性值都是来自于项目的application.yml文件的配置信息
 * <h3>使用限制</h3>
 * <p>由于本框架不是一个类似于spring的容器框架，不是所有与的类型都是被框架所管理的，
 * 所以那些不被框架框里的类型是无法注入配置属性的，切记！
 * </p>
 * <h3>参考资料</h3>
 * <a href="https://blog.csdn.net/m0_37556444/article/details/113513089">推断main函数所在的类（来自于spring boot)</a>
 * <a href="https://www.codenong.com/11306811/">获取方法调用者所在的类</a>
 * @see ServerConfiguration
 * @see com.nf.mvc.MvcContext
 * @see ConfigurationProperties
 * @author cj
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface EnableConfigurationProperties {
    Class<?>[] value() default {};
}
