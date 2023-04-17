package controller;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * loadOnStartup设置为正数，就是tomcat启动时，此servlet就会被初始化
 * 否则是第一次请求时就会被初始化（实例化）
 *
 * 后续每一次请求，直接交给service方法去处理请求
 *
 *
 * 项目从tomcat卸载或者tomcat停止时，所有的servlet就会被销毁
 * 所以，destroy就会被得到执行
 *
 * 每一个servlet事例是tomcat容器帮我们创建出来的，而且只有一个，
 *
 * 但是，每一个请求，通常情况下是一个不同的线程来执行这一段代码
 *
 */
//@WebServlet(value = "/first")
public class FirstServlet extends HttpServlet {


    @Override
    public void init(ServletConfig config) throws ServletException {
        System.out.println("---------");
        String aa = config.getInitParameter("aa");
        String bb = config.getInitParameter("bb");
        System.out.println("aa = " + aa);
        System.out.println("bb = " + bb);
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("service*****");


    }

    @Override
    public void destroy() {
        System.out.println("destroy---------------");
    }
}
