package com.nf.mvc;


import com.nf.mvc.configuration.ServerConfiguration;
import com.nf.mvc.ioc.Autowired;
import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import javax.servlet.MultipartConfigElement;
import java.io.File;
import java.time.LocalTime;

/**
 * 嵌入式Tomcat启动类，这点是模仿spring boot的行为来实现的,可以让mvc框架直接通过入口函数的方式来启动
 * <h3>基本使用</h3>
 * 编写类似下面这样的代码，直接运行就可以启动mvc项目
 * <pre class="code">
 *     public static void main(String[] args) {
 *         MvcApplication.run(args);
 *     }
 * </pre>
 * <h3>参数设定</h3>
 * 默认情况下，有如下默认值
 * <ul>
 *     <li>contextPath=/mvc</li>
 *     <li>port=8080</li>
 *     <li>basePackage=mvcdemo</li>
 *     <li>urlPattern=/</li>
 *     <li>文件上传时使用的临时目录=System.getProperty("java.io.tmpdir")</li>
 * </ul>
 * <h3>使用限制</h3>
 * <ul>
 *     <li>暂时没有处理默认servlet的问题，所以静态资源采用默认servlet的方式就不可行</li>
 * </ul>
 * <h3>参考资料</h3>
 * <a href="https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat">嵌入式tomcat</a>
 * <a href="https://www.cnblogs.com/develon/p/11602969.html">嵌入式tomcat以及集成spring</a>
 * <a href="https://www.cnblogs.com/pilihaotian/p/8822926.html">spring boot 处理jsp的分析</a>
 * <i>此类的编写参考了spring boot中的TomcatServletWebServerFactory中的代码(重点是getWebServer方法)</i>
 * @author cj
 */
public class MvcApplication {
    @Autowired
    private ServerConfiguration serverConfiguration;

    public static final String CONTEXTPATH = "contextPath";
    public static final String PORT = "port";
    public static final String BASEPACKAGE = "basePackage";
    public static final String URLPATTERN = "urlPattern";

    private static final String CONTEXTPATH_DEFAULT = "/mvc";
    private static final int PORT_DEFAULT = 8080;
    private static final String BASEPACKAGE_DEFAULT = "mvcdemo";
    private static final String URLPATTERN_DEFAULT = "/";
    private static final String TEMPDIR_DEFAULT = System.getProperty("java.io.tmpdir");


    private  String contextPath ;
    private int port;
    private  String basePackage;
    private  String urlPattern;


    public static void run(String... args) {
        new MvcApplication().start(args);
    }

    private void start(String... args) {
        parseArgs(args);

        Tomcat tomcat = new Tomcat();
        tomcat.setPort(port);
        String docBase = new File(".").getAbsolutePath();
        System.out.println("项目的目录为: " + docBase);

        Context ctx = registerContext(tomcat, docBase);
        registerDispatcherServlet(ctx);
        registerShutdownHook(tomcat);

        startEmbededTomcat(tomcat);
    }

    private void parseArgs(String... args) {
        // 先赋值为默认值
        contextPath = CONTEXTPATH_DEFAULT;
        port = PORT_DEFAULT;
        basePackage = BASEPACKAGE_DEFAULT;
        urlPattern = URLPATTERN_DEFAULT;

        //解析参数中设定的值
        for (String arg : args) {
            String[] argument = arg.split("=");
            String key = argument[0];
            String value = argument[1];
            if (CONTEXTPATH.equalsIgnoreCase(key)) {
                contextPath = value;
            }
            if (PORT.equalsIgnoreCase(key)) {
                port = Integer.parseInt(value);
            }
            if (BASEPACKAGE.equalsIgnoreCase(key)) {
                basePackage = value;
            }
            if (URLPATTERN.equalsIgnoreCase(key)) {
                urlPattern = value;
            }
        }
    }

    private void startEmbededTomcat(Tomcat tomcat) {
        try {
            tomcat.start();
            printBanner();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    private Context registerContext(Tomcat tomcat, String docBase) {
        // 在这篇文章里有说明addContext与addWebApp的区别https://stackoverflow.com/questions/67253024/tomcat-catalina-context-add-existing-servlet-to-context
        // 如果仅仅只是把addContext方法换成addWebApp方法会导致DispatcherServlet注册url-pattern为/失效
        Context ctx = tomcat.addContext(contextPath, docBase);
        return ctx;
    }

    private void registerDispatcherServlet(Context ctx) {
        Wrapper wrapper = Tomcat.addServlet(ctx, "dispatcherServlet", new DispatcherServlet());
        ctx.addServletMappingDecoded(urlPattern, "dispatcherServlet");

        wrapper.addInitParameter("base-package", basePackage);
        wrapper.setMultipartConfigElement(new MultipartConfigElement(TEMPDIR_DEFAULT));
        wrapper.setLoadOnStartup(1);
    }

    private void registerShutdownHook(Tomcat tomcat) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        tomcat.destroy();
                    } catch (LifecycleException e) {
                        e.printStackTrace();
                    }
                })
        );
    }

    private void printBanner() {
        //ascii 文本生成是借助于https://tools.kalvinbg.cn/txt/ascii提供的工具生成(采用starwars字体)
        String bannerText = "  ______  __    __   _______ .__   __.        __   __    __  .__   __. \n" +
                " /      ||  |  |  | |   ____||  \\ |  |       |  | |  |  |  | |  \\ |  | \n" +
                "|  ,----'|  |__|  | |  |__   |   \\|  |       |  | |  |  |  | |   \\|  | \n" +
                "|  |     |   __   | |   __|  |  . `  | .--.  |  | |  |  |  | |  . `  | \n" +
                "|  `----.|  |  |  | |  |____ |  |\\   | |  `--'  | |  `--'  | |  |\\   | \n" +
                " \\______||__|  |__| |_______||__| \\__|  \\______/   \\______/  |__| \\__| \n" +
                "                                                                       ";
        System.out.println(bannerText);
        System.out.println("=======================================================================");
        System.out.println("Mvc框架项目启动成功了:" + LocalTime.now());
        System.out.println(PORT + ":" + this.port);
        System.out.println(CONTEXTPATH + ":" + this.contextPath);
        System.out.println(URLPATTERN + ":" + this.urlPattern);
        System.out.println(BASEPACKAGE + ":" + this.basePackage);
        System.out.println("文件上传用的临时目录:" + ":" + TEMPDIR_DEFAULT);
        System.out.println("=======================================================================");
    }
}
