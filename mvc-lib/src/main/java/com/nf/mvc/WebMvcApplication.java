package com.nf.mvc;


import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Wrapper;
import org.apache.catalina.startup.Tomcat;

import java.io.File;

/**
 * TODO:嵌入式Tomcat
 *
 *
 * <h3>参考资料</h3>
 * <a href="https://devcenter.heroku.com/articles/create-a-java-web-application-using-embedded-tomcat">嵌入式tomcat</a>
 *
 * @author cj
 */
public class WebMvcApplication {
    public static void run() {
        Tomcat tomcat = new Tomcat();
        tomcat.setPort(8080);
        // 这种写法意味着web资源放置在src/main/webapp目录里 TODO
        String webAppBaseDir = new File("src/main/webapp/").getAbsolutePath();
        System.out.println("configuring app with basedir: " + new File("./" + webAppBaseDir).getAbsolutePath());
        Context ctx = tomcat.addWebapp("/mvc", webAppBaseDir);

        Wrapper wrapper = Tomcat.addServlet(ctx, "dispatcherServlet", new DispatcherServlet());
        ctx.addServletMappingDecoded("/d", "dispatcherServlet");

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
            tomcat.getServer().await();
        } catch (LifecycleException e) {
            e.printStackTrace();
        }
    }

}
