package com.nf.mvc;

import com.nf.mvc.adapters.HttpRequestHandlerAdapter;

import com.nf.mvc.mappings.NameConventionHandlerMapping;
import com.nf.mvc.util.ScanUtils;
import com.nf.mvc.view.VoidViewResult;
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
    /** 此选项是用来配置要扫描的类所在的包的，在DispatcherServlet的init-param里面进行配置 */
    private static final String COMPONENT_SCAN = "componentScan";
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    //region 初始化逻辑
    @Override
    public void init(ServletConfig config) throws ServletException {
        //System.out.println("DispatcherServlet this.getClass().getClassLoader() = " + this.getClass().getClassLoader());

        //获取要扫描的类所在的包的名字
        String scanPackage = getScanPackage(config);
        //去执行类扫描的功能
        ScanResult scanResult = ScanUtils.scan(scanPackage);

        initMvcContext(scanResult);
        initHandlerMappings();
        initHandlerAdapters();


    }

    private void initMvcContext(ScanResult scanResult) {
        MvcContext.getMvcContext().config(scanResult);
    }

    private void initHandlerMappings() {
        //优先添加用户自定义的HandlerMapping
        List<HandlerMapping> customHandlerMappings = getCustomHandlerMappings();
        //mvc框架自身的HandlerMapping优先级更低，后注册
        List<HandlerMapping> defaultHandlerMappings = getDefaultHandlerMappings();

        handlerMappings.addAll(customHandlerMappings);
        handlerMappings.addAll(defaultHandlerMappings);
    }

    protected List<HandlerMapping> getCustomHandlerMappings() {
        return MvcContext.getMvcContext().getHandlerMappings();
    }

    protected List<HandlerMapping> getDefaultHandlerMappings() {
        List<HandlerMapping> mappings = new ArrayList<>();
        mappings.add(new NameConventionHandlerMapping());
        return mappings;
    }

    private void initHandlerAdapters() {

        //优先添加用户自定义的HandlerAdapter
        List<HandlerAdapter> customHandlerAdapters = getCustomHandlerAdapters();
        //mvc框架自身的HandlerAdapter优先级更低，后注册
        List<HandlerAdapter> defaultHandlerAdapters = getDefaultHandlerAdapters();

        handlerAdapters.addAll(customHandlerAdapters);
        handlerAdapters.addAll(defaultHandlerAdapters);

    }

    protected List<HandlerAdapter> getCustomHandlerAdapters() {
        return MvcContext.getMvcContext().getHandlerAdapters();
    }

    protected List<HandlerAdapter> getDefaultHandlerAdapters() {
        List<HandlerAdapter> adapters = new ArrayList<>();
        adapters.add(new HttpRequestHandlerAdapter());
        // handlerAdapters.add(new MethodNameHandlerAdapter());
        return adapters;
    }

    private String getScanPackage(ServletConfig config) {
        String pkg = config.getInitParameter(COMPONENT_SCAN);

        if (pkg == null || pkg.isEmpty()) {
            throw new IllegalStateException("必须指定扫描的包，此包是控制器或者是其它扩展组件所在的包---");
        }
        return pkg;
    }

    //endregion
    //region 请求处理逻辑

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
        try {
            Object handler = getHandler(req);
            if (handler != null) {
                doService(req, resp, handler);
            } else {
                noHandlerFound(req, resp);
            }
        }catch (ServletException | IOException ex) {
            throw ex;
        } catch (Exception ex) {
            System.out.println("对请求进行处理时出错------" + ex.getMessage());
        }
    }

    protected Object getHandler(HttpServletRequest request) throws Exception {
        for (HandlerMapping mapping : handlerMappings) {
            Object handler = mapping.getHandler(request);
            if (handler != null) {
                return handler;
            }
        }
        return null;
    }

    protected void doService(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerAdapter adapter = getHandlerAdapter(handler);
        ViewResult viewResult = adapter.handle(req, resp, handler);
        render(req, resp, viewResult);
    }

    protected void noHandlerFound(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        resp.sendError(HttpServletResponse.SC_NOT_FOUND);
    }

    protected void render(HttpServletRequest req, HttpServletResponse resp, ViewResult viewResult) throws Exception {
        //如果handler的方法返回void或者返回null，
        // 我们框架就自动的帮你封装成VoidView，
        //这样做的目的是想让handler的作者在写方法时，不限制必须返回ViewResult类型
        if (viewResult == null) {
            viewResult = new VoidViewResult();
        }
        viewResult.render(req, resp);
    }

    protected HandlerAdapter getHandlerAdapter(Object handler) throws ServletException {
        for (HandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }
        throw new ServletException("此Handler没有对应的adapter去处理，请在DispatcherServlet中进行额外的配置");
    }

    //endregion
}
