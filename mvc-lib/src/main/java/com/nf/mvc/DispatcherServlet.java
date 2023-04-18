package com.nf.mvc;

import com.nf.mvc.adapters.HttpRequestHandlerAdapter;
import com.nf.mvc.mappings.NameConventionHandlerMapping;
import com.nf.mvc.util.ScanUtils;
import com.nf.mvc.view.VoidView;
import io.github.classgraph.ClassInfoList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DispatcherServlet extends HttpServlet {

    private static final String COMPONENT_SCAN = "componentScan";

    HandlerMapping handlerMapping ;
    HandlerAdapter handlerAdapter;


    @Override
    public void init(ServletConfig config) throws ServletException {
        String scanPackage = config.getInitParameter(COMPONENT_SCAN);

        ClassInfoList classInfos = ScanUtils.scan(scanPackage);

         handlerMapping = new NameConventionHandlerMapping(classInfos);
         handlerAdapter = new HttpRequestHandlerAdapter();


    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException,IOException {


        try {
            Object handler = handlerMapping.getHandler(req);
            ViewResult viewResult = new VoidView();
            if (handlerAdapter.supports(handler)) {
                 viewResult = handlerAdapter.handle(req, resp, handler);
            }

            viewResult.render(req, resp);
        } catch (Exception ex) {
            System.out.println("yichang dispatcher------");
        }


    }
}
