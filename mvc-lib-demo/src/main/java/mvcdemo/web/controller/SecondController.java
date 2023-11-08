package mvcdemo.web.controller;

import com.nf.mvc.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecondController implements HttpRequestHandler {
    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //req.getParameter("asdf");
        //表单提交时，有多个控件名字一样
        String[] asdfs = req.getParameterValues("asdf");


        System.out.println("2222------");
        resp.getWriter().println("second ----");

    }
}
