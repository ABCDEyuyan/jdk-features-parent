package com.nf.mvc;


import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * TODO:嵌入式Tomcat
 * <i>此类的编写参考了spring boot中的TomcatServletWebServerFactory中的代码(重点是getWebServer方法)</i>
 * <h3>参考资料</h3>
 * <a href="https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat">嵌入式tomcat</a>
 * <a href="https://www.cnblogs.com/develon/p/11602969.html">嵌入式tomcat以及集成spring</a>
 * <a href="https://www.cnblogs.com/pilihaotian/p/8822926.html">spring boot 处理jsp的分析</a>
 *
 * @author cj
 */
public class MvcApplication {
    public static void run() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        // 这种写法意味着web资源放置在src/main/webapp目录里 TODO
        String docBase = new File(".").getAbsolutePath();
        System.out.println("项目的目录为: " + new File("." + docBase).getAbsolutePath());
        // 在这篇文章里有说明addContext与addWebApp的区别https://stackoverflow.com/questions/67253024/tomcat-catalina-context-add-existing-servlet-to-context
        // 如果仅仅只是把addContext方法换成addWebApp方法会导致DispatcherServlet注册url-pattern为/失效
        Context ctx = tomcat.addContext("/mvc", docBase);

        Wrapper wrapper = Tomcat.addServlet(ctx, "dispatcherServlet", new DispatcherServlet());
        ctx.addServletMappingDecoded("/", "dispatcherServlet");

        wrapper.addInitParameter("base-package", "mvcdemo");
        wrapper.setLoadOnStartup(1);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        tomcat.destroy();
                    } catch (LifecycleException e) {
                        e.printStackTrace();
                    }
                })
        );

        try {
            tomcat.start();
            printBanner();
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用来获取入口函数所在的类,代码来自于spring boot框架中的SpringApplication类
     * @return main入口方法所在类
     */
    private Class<?> deduceMainApplicationClass() {
        try {
            StackTraceElement[] stackTrace = new RuntimeException().getStackTrace();
            for (StackTraceElement stackTraceElement : stackTrace) {
                if ("main".equals(stackTraceElement.getMethodName())) {
                    return Class.forName(stackTraceElement.getClassName());
                }
            }
        }
        catch (ClassNotFoundException ex) {
            // Swallow and continue
        }
        return null;
    }

    private static void printBanner(){
        //ascii 文本生成是借助于https://tools.kalvinbg.cn/txt/ascii提供的工具生成(采用starwars字体)
        String bannerText = "  ______  __    __   _______ .__   __.        __   __    __  .__   __. \n" +
                " /      ||  |  |  | |   ____||  \\ |  |       |  | |  |  |  | |  \\ |  | \n" +
                "|  ,----'|  |__|  | |  |__   |   \\|  |       |  | |  |  |  | |   \\|  | \n" +
                "|  |     |   __   | |   __|  |  . `  | .--.  |  | |  |  |  | |  . `  | \n" +
                "|  `----.|  |  |  | |  |____ |  |\\   | |  `--'  | |  `--'  | |  |\\   | \n" +
                " \\______||__|  |__| |_______||__| \\__|  \\______/   \\______/  |__| \\__| \n" +
                "                                                                       ";
        System.out.println(bannerText);
    }
}
