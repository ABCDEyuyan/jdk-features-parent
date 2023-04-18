package controller;

import com.nf.mvc.HttpRequestHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ThirdController  {

    public void processRequest(HttpServletRequest req, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("333-----------");
        response.getWriter().println("third ----");
    }
}
