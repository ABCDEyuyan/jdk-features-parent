package com.nf.mvc;

import com.nf.mvc.adapter.HttpRequestHandlerAdapter;

import com.nf.mvc.adapter.RequestMappingHandlerAdapter;
import com.nf.mvc.argument.ComplexTypeMethodArguementResolver;
import com.nf.mvc.argument.MultipartFileMethodArgumentResolver;
import com.nf.mvc.argument.RequestBodyMethodArguementResolver;
import com.nf.mvc.argument.SimpleTypeMethodArguementResolver;
import com.nf.mvc.exception.ExceptionHandlerExceptionResolver;
import com.nf.mvc.mapping.NameConventionHandlerMapping;
import com.nf.mvc.mapping.RequestMappingHandlerMapping;
import com.nf.mvc.support.HttpMethod;
import com.nf.mvc.util.ScanUtils;
import io.github.classgraph.ScanResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.nf.mvc.handler.HandlerHelper.empty;

public class DispatcherServlet extends HttpServlet {
    /**
     * 此选项是用来配置要扫描的类所在的包的，在DispatcherServlet的init-param里面进行配置
     */
    private static final String COMPONENT_SCAN = "componentScan";
    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();

    //region 初始化逻辑
    @Override
    public void init(ServletConfig config) throws ServletException {
        //System.out.println("DispatcherServlet this.getClass().getClassLoader() = " + this.getClass().getClassLoader());

        //获取要扫描的类所在的包的名字
        String scanPackage = getScanPackage(config);
        //去执行类扫描的功能
        ScanResult scanResult = ScanUtils.scan(scanPackage);

        initMvcContext(scanResult);
        //参数解析器因为被Adapter使用，所以要在adapter初始化之前进行
        initArgumentResolvers();
        initHandlerMappings();
        initHandlerAdapters();
        initExceptionResolvers();

    }

    private void initMvcContext(ScanResult scanResult) {
        MvcContext.getMvcContext().config(scanResult);
    }

    private void initArgumentResolvers() {

        List<MethodArgumentResolver> customArgumentResolvers = getCustomArgumentResolvers();

        List<MethodArgumentResolver> defaultArgumentResolvers = getDefaultArgumentResolvers();

        argumentResolvers.addAll(customArgumentResolvers);
        argumentResolvers.addAll(defaultArgumentResolvers);
        //把定制+默认的所有HandlerMapping组件添加到上下文中
        MvcContext.getMvcContext().setArgumentResolvers(argumentResolvers);
    }

    protected List<MethodArgumentResolver> getDefaultArgumentResolvers() {
        List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();
        //RequestBody解析器要放在复杂类型解析器之前，基本上简单与复杂类型解析器应该放在最后
        argumentResolvers.add(new MultipartFileMethodArgumentResolver());
        argumentResolvers.add(new RequestBodyMethodArguementResolver());
        argumentResolvers.add(new SimpleTypeMethodArguementResolver());
        argumentResolvers.add(new ComplexTypeMethodArguementResolver());

        return argumentResolvers;
    }

    protected List<MethodArgumentResolver> getCustomArgumentResolvers() {
        return MvcContext.getMvcContext().getCustomArgumentResolvers();
    }

    private void initHandlerMappings() {
        //优先添加用户自定义的HandlerMapping
        List<HandlerMapping> customHandlerMappings = getCustomHandlerMappings();
        //mvc框架自身的HandlerMapping优先级更低，后注册
        List<HandlerMapping> defaultHandlerMappings = getDefaultHandlerMappings();

        handlerMappings.addAll(customHandlerMappings);
        handlerMappings.addAll(defaultHandlerMappings);
        //把定制+默认的所有HandlerMapping组件添加到上下文中
        MvcContext.getMvcContext().setHandlerMappings(handlerMappings);
    }

    protected List<HandlerMapping> getCustomHandlerMappings() {
        return MvcContext.getMvcContext().getCustomHandlerMappings();
    }

    protected List<HandlerMapping> getDefaultHandlerMappings() {
        List<HandlerMapping> mappings = new ArrayList<>();
        mappings.add(new RequestMappingHandlerMapping());
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
        MvcContext.getMvcContext().setHandlerAdapters(handlerAdapters);

    }

    protected List<HandlerAdapter> getCustomHandlerAdapters() {
        return MvcContext.getMvcContext().getCustomHandlerAdapters();
    }

    protected List<HandlerAdapter> getDefaultHandlerAdapters() {
        List<HandlerAdapter> adapters = new ArrayList<>();
        adapters.add(new RequestMappingHandlerAdapter());
        adapters.add(new HttpRequestHandlerAdapter());
        // handlerAdapters.add(new MethodNameHandlerAdapter());
        return adapters;
    }

    private void initExceptionResolvers() {
        //优先添加用户自定义的HandlerMapping
        List<HandlerExceptionResolver> customExceptionResolvers = getCustomExceptionResolvers();
        //mvc框架自身的HandlerMapping优先级更低，后注册
        List<HandlerExceptionResolver> defaultExceptionResolvers = getDefaultExceptionResolvers();

        exceptionResolvers.addAll(customExceptionResolvers);
        exceptionResolvers.addAll(defaultExceptionResolvers);
        //把定制+默认的所有HandlerMapping组件添加到上下文中
        MvcContext.getMvcContext().setExceptionResolvers(exceptionResolvers);
    }

    protected List<HandlerExceptionResolver> getCustomExceptionResolvers() {
        return MvcContext.getMvcContext().getCustomExceptionResolvers();
    }

    protected List<HandlerExceptionResolver> getDefaultExceptionResolvers() {
        List<HandlerExceptionResolver> resolvers = new ArrayList<>();
        //resolvers.add(new LogHandlerExceptionResolver());
        //resolvers.add(new PrintStackTraceHandlerExceptionResolver());
        resolvers.add(new ExceptionHandlerExceptionResolver());
        return resolvers;
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
     * service的方法，由于是重写父类型的方法，其签名是没有办法改变
     * 比如改成throws Throwable，这是不行的
     *
     * 所以就增加了一个doService的方法，以便有机会改doService的签名
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setEncoding(req, resp);
        allowCors(req,resp,new CorsConfiguration());
        doService(req, resp);
    }

    /**
     * 因为我们是模仿spring mvc，mvc里面它只是对Handler的执行进行了
     * 全局异常的处理，也就是HandlerMapping，HandlerAdapter，以及Handler
     * 本身的异常才会处理
     *
     * 但是render的流程是没有进行异常处理
     * @param req
     * @param resp
     */
    protected void doService(HttpServletRequest req, HttpServletResponse resp) {
        Object handler;
        try {
            //这行代码也表明HandlerMapping在查找Handler的过程中出了异常是没有被我们的异常解析器处理的
            handler = getHandler(req);
            if (handler != null) {
                doDispatch(req, resp, handler);
            } else {
                noHandlerFound(req, resp);
            }
        } catch (Throwable ex) {
            //spring mvc在这个地方是做了额外的异常处理的
            //下面的代码不应该这些写printStackTrace，这里写上主要是我们开发测试用的
            System.out.println("可以在这里再做一层异常处理，比如处理视图渲染方面的异常等，但现在什么都没做,异常消息是:" + ex.getMessage());
            ex.printStackTrace();
        }
    }

    protected void doDispatch(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Throwable {
        ViewResult viewResult;
        try {
            HandlerAdapter adapter = getHandlerAdapter(handler);
            viewResult = adapter.handle(req, resp, handler);
        } catch (Exception ex) {
            //这里只处理Exception，非Exception并没有处理，会继续抛出给doService处理
            //这个异常处理也只是处理了Handler整个执行层面的异常，
            // 视图渲染层面的异常是没有处理的，要处理的话可以在doService方法里处理
            viewResult = resolveException(req, resp, handler, ex);
        }
        render(req, resp, viewResult);
    }

    protected ViewResult resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) throws Exception{
        for (HandlerExceptionResolver exceptionResolver : exceptionResolvers) {
            Object result = exceptionResolver.resolveException(req, resp, handler, ex);
            if (result != null) {
                return (ViewResult) result;
            }
        }
        /*表示没有一个异常解析器可以处理异常，那么就应该把异常继续抛出*/
       throw ex;
    }

    /**
     * 设置编码的方法是在service方法里面第一个调用，如果已经从req
     * 对象中获取数据了，再设置这个编码是无效
     *
     * @param req
     * @param resp
     * @throws Exception
     */
    protected void setEncoding(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");
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

    protected void noHandlerFound(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        //我们获取容器中本来就有的默认servlet来处理静态资源
        //容器中默认servlet是有能力处理静态资源
        //默认servlet的名字，在很多容器中就是叫default，但有些容器不叫default
        //常用的tomcat，jetty这些容器中就是叫default
        req.getServletContext().getNamedDispatcher("default").forward(req, resp);
    }

    protected void render(HttpServletRequest req, HttpServletResponse resp, ViewResult viewResult) throws Exception {
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

    protected void allowCors(HttpServletRequest req,HttpServletResponse resp,CorsConfiguration configuration){
        // 解决跨域请求问题
        String origin = req.getHeader (CorsConfiguration.ORIGIN);
        if (origin == null) {
            origin = req.getHeader (CorsConfiguration.REFERER);
        }
        // 允许指定域访问跨域资源
        resp.setHeader(CorsConfiguration.ACCESS_CONTROL_ALLOW_ORIGIN,origin);
        // 允许客户端携带跨域cookie，此时origin值不能为“*”，只能为指定单一域名
        resp.setHeader(CorsConfiguration.ACCESS_CONTROL_ALLOW_CREDENTIALS,CorsConfiguration.TRUE);

        if (HttpMethod.OPTIONS.matches(req.getMethod ())) {
            String allowMethod = req.getHeader (CorsConfiguration.ACCESS_CONTROL_REQUEST_METHOD);
            String allowHeaders = req.getHeader (CorsConfiguration.ACCESS_CONTROL_REQUEST_HEADERS);
            // 浏览器缓存预检请求结果时间,单位:秒
            resp.setHeader(CorsConfiguration.ACCESS_CONTROL_MAX_AGE, CorsConfiguration.CACHE_86400);
            // 允许浏览器在预检请求成功之后发送的实际请求方法名
            resp.setHeader(CorsConfiguration.ACCESS_CONTROL_ALLOW_METHODS, allowMethod);
            // 允许浏览器发送的请求消息头
            resp.setHeader(CorsConfiguration.ACCESS_CONTROL_ALLOW_HEADERS, allowHeaders);
            return;
        }
    }
    //endregion
}
