package com.nf.mvc;

import com.nf.mvc.adapters.HttpRequestHandlerAdapter;

import com.nf.mvc.mappings.NameConventionHandlerMapping;
import com.nf.mvc.util.ScanUtils;
import com.nf.mvc.view.VoidView;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DispatcherServlet extends HttpServlet {

    private static final String COMPONENT_SCAN = "componentScan";

    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    @Override
    public void init(ServletConfig config) throws ServletException {

        String scanPackage = getScanPackage(config);

        ScanResult scanResult = ScanUtils.scan(scanPackage);
        MvcContext.getMvcContext().config(scanResult);


        initHandlerMappings();
        initHandlerAdapters();


    }

    private void initHandlerMappings() {
        handlerMappings.add(new NameConventionHandlerMapping());
    }

    private void initHandlerAdapters() {

        //优先添加用户自定义的HandlerAdapter
        ScanResult scanResult = MvcContext.getMvcContext().getScanResult();
        ClassInfoList allClasses = scanResult.getAllClasses();
        for (ClassInfo classInfo : allClasses) {
            Class<?> scanedClass = classInfo.loadClass();
            if (HandlerAdapter.class.isAssignableFrom(scanedClass)) {
                HandlerAdapter adapter = null;
                try {
                    adapter = (HandlerAdapter)scanedClass.newInstance();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                handlerAdapters.add(adapter);
            }
        }

        //mvc框架自身的HandlerAdapter优先级更低，后注册
        handlerAdapters.add(new HttpRequestHandlerAdapter());
       // handlerAdapters.add(new MethodNameHandlerAdapter());

    }

    private String getScanPackage(ServletConfig config) {
        String pkg = config.getInitParameter(COMPONENT_SCAN);

        if (pkg == null || pkg.isEmpty()) {
            throw new IllegalStateException("必须指定扫描的包，此包是控制器或者是其它扩展组件所在的包---");
        }
        return pkg;
    }

    /**
     * 方法通常会包含很多核心逻辑步骤，如果每一个逻辑步骤都有一些零散的代码
     * 如果都放在一起，就会导致本方法代码很长，不容易看懂，
     * 所以，最好是把其中一个个的核心逻辑用单独的方法封装起来
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Object handler = getHandler(req);
        if (handler != null) {
            doService(req, resp, handler);
        } else {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            //发送一个404的错误之后，就不需要再走后续流程，所以要return
            return;
        }

    }

    private Object getHandler(HttpServletRequest request) {

        try {
            for (HandlerMapping mapping : handlerMappings) {
                Object handler = mapping.getHandler(request);
                if (handler != null) {
                   return  handler;
                }
            }

        } catch (Exception e) {
            throw new IllegalStateException("针对此请求查找handler的时候出错");
        }
        return null;
    }

    private void render(HttpServletRequest req, HttpServletResponse resp, ViewResult viewResult) {
        //如果handler的方法返回void或者返回null，
        // 我们框架就自动的帮你封装成VoidView，
        //这样做的目的是想让handler的作者在写方法时，不限制必须返回ViewResult类型
        if (viewResult == null) {
            viewResult = new VoidView();
        }
        viewResult.render(req, resp);
    }

    private void doService(HttpServletRequest req, HttpServletResponse resp, Object handler) throws ServletException, IOException {
        try {
            HandlerAdapter adapter = getHandlerAdapter(handler);
            ViewResult viewResult = adapter.handle(req, resp, handler);
            render(req, resp, viewResult);
        } catch (Exception ex) {
            System.out.println("yichang dispatcher------" + ex.getMessage());
        }
    }

    private HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new ServletException("此Handler没有对应的adapter去处理，请在DispatcherServlet中进行额外的配置");
    }
}
