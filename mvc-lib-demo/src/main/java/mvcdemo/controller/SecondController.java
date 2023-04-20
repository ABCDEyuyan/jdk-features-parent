package mvcdemo.controller;

import com.nf.mvc.HttpRequestHandler;
import com.nf.mvc.ViewResult;
import com.nf.mvc.view.ForwardViewResult;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SecondController implements HttpRequestHandler {
    @Override
    public void processRequest(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("2222------");
        response.getWriter().println("second ----");

    }
}
