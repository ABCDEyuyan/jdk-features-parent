package com.nf.mvc;

import com.nf.mvc.adapter.HttpRequestHandlerAdapter;
import com.nf.mvc.adapter.RequestMappingHandlerAdapter;
import com.nf.mvc.argument.*;
import com.nf.mvc.exception.ExceptionHandlerExceptionResolver;
import com.nf.mvc.exception.LogHandlerExceptionResolver;
import com.nf.mvc.exception.PrintStackTraceHandlerExceptionResolver;
import com.nf.mvc.mapping.NameConventionHandlerMapping;
import com.nf.mvc.mapping.RequestMappingHandlerMapping;
import com.nf.mvc.support.HttpHeaders;
import com.nf.mvc.support.HttpMethod;
import com.nf.mvc.util.CorsUtils;
import com.nf.mvc.util.ScanUtils;
import com.nf.mvc.util.StringUtils;
import io.github.classgraph.ScanResult;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class DispatcherServlet extends HttpServlet {
    /**
     * 此选项是用来配置要扫描的类所在的基础包的，在DispatcherServlet的init-param里面进行配置
     */
    private static final String BASE_PACKAGE = "base-package";
    private final List<HandlerMapping> handlerMappings = new ArrayList<>();
    private final List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private final List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();
    private final List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();

    // 用这种方式实例化是因为其调用了applyDefaultConfiguration方法，创建出来的对象是有了一些默认设置的
    // 因为不管有没有配置器对跨域进行定制配置，都要全局进行跨域支持的处理
    private final CorsConfiguration corsConfiguration =  CorsConfiguration.defaultInstance();

    //region 初始化逻辑
    @Override
    public void init(ServletConfig config) throws ServletException {
        //System.out.println("DispatcherServlet this.getClass().getClassLoader() = " + this.getClass().getClassLoader());

        //获取要扫描的类所在的包的名字
        String basePackage = getBasePackage(config);
        //去执行类扫描的功能
        ScanResult scanResult = ScanUtils.scan(basePackage);
        initMvcContext(scanResult);
        initMvc();
        configMvc();

    }

    private void initMvc() {
        /* 参数解析器因为被Adapter使用，所以其初始化要在adapter初始化之前进行 */
        initArgumentResolvers();
        initHandlerMappings();
        initHandlerAdapters();
        initExceptionResolvers();
    }

    private void configMvc() {
        MvcContext mvcContext = MvcContext.getMvcContext();
        WebMvcConfigurer mvcConfigurer = mvcContext.getCustomWebMvcConfigurer();
        if (mvcConfigurer == null) {
            return;
        }

        //由于这些配置方法子类可以重写，所以给这些方法都设置了参数，便于重写，是可以不用添加任何参数的
        configArgumentResolvers(MvcContext.getMvcContext().getArgumentResolvers(),mvcConfigurer);
        configHandlerMappings(MvcContext.getMvcContext().getHandlerMappings(),mvcConfigurer);
        configHandlerAdapters(MvcContext.getMvcContext().getHandlerAdapters(),mvcConfigurer);
        configExceptionResolvers(MvcContext.getMvcContext().getExceptionResolvers(),mvcConfigurer);
        //由于corsConfiguration对象是有了默认值设置的实例，没有配置器的时候不配置cors也能用默认设置处理跨域
        configGlobalCors(this.corsConfiguration,mvcConfigurer);
    }

    protected void configArgumentResolvers(List<MethodArgumentResolver> argumentResolvers,WebMvcConfigurer mvcConfigurer) {
        executeMvcComponentsConfig(argumentResolvers,mvcConfigurer::configureArgumentResolver);
    }

    protected void configHandlerMappings(List<HandlerMapping> handlerMappings,WebMvcConfigurer mvcConfigurer) {
        executeMvcComponentsConfig(handlerMappings,mvcConfigurer::configureHandlerMapping);
    }

    protected void configHandlerAdapters(List<HandlerAdapter> handlerAdapters,WebMvcConfigurer mvcConfigurer) {
        executeMvcComponentsConfig(handlerAdapters,mvcConfigurer::configureHandlerAdapter);
    }

    protected void configExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers,WebMvcConfigurer mvcConfigurer) {
        executeMvcComponentsConfig(exceptionResolvers,mvcConfigurer::configureExceptionResolver);
    }

    protected void configGlobalCors(CorsConfiguration configuration,WebMvcConfigurer mvcConfigurer) {
        // 不需要再调用默认设置，全局实例化时已经设置过了，如果用户不需要这些默认设置，可以调用clearDefaultConfiguration方法进行清除
        //configuration.applyDefaultConfiguration();
        mvcConfigurer.configureCors(configuration);
        // mvcConfigurer.configureCors(configuration) 这行代码你也可以换成像下面这样写
        // executeMvcComponentsConfig(Arrays.asList(configuration),mvcConfigurer::configureCors);
    }

    private <T> void executeMvcComponentsConfig(List<T> mvcComponents, Consumer<T> consumer) {
        mvcComponents.forEach(consumer);
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
        argumentResolvers.add(new ServletApiMethodArgumentResolver());
        argumentResolvers.add(new MultipartFileMethodArgumentResolver());
        //RequestBody解析器要放在复杂类型解析器之前，基本上简单与复杂类型解析器应该放在最后
        argumentResolvers.add(new RequestBodyMethodArguementResolver());
        argumentResolvers.add(new SimpleTypeMethodArguementResolver());
        argumentResolvers.add(new ComplexTypeMethodArgumentResolver());

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
        resolvers.add(new LogHandlerExceptionResolver());
        resolvers.add(new PrintStackTraceHandlerExceptionResolver());
        resolvers.add(new ExceptionHandlerExceptionResolver());
        return resolvers;
    }


    private String getBasePackage(ServletConfig config) {
        String pkg = config.getInitParameter(BASE_PACKAGE);

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
     * <p>
     * service的方法，由于是重写父类型的方法，其签名是没有办法改变
     * 比如改成throws Throwable，这是不行的
     * <p>
     * 所以就增加了一个doService的方法，以便有机会改doService的签名
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setEncoding(req, resp);
        if (CorsUtils.isCorsRequest(req)) {
            processCors(req, resp, corsConfiguration);
            /*如果是预检请求需要return，以便及时响应预检请求，以便处理后续的真正请求*/
            if (CorsUtils.isPreFlightRequest(req)) {
                return;
            }
        }
        doService(req, resp);
    }


    protected void doService(HttpServletRequest req, HttpServletResponse resp) {
        HandlerExecutionChain chain;
        HandlerContext context = HandlerContext.getContext();
        context.setRequest(req).setResponse(resp);
        try {

            /*这行代码也表明HandlerMapping在查找Handler的过程中出了异常是没有被我们的异常解析器处理的
             * Hanlder的异常解析处理是在doDispatch方法中实现的*/
            chain = getHandler(req);
            if (chain != null) {
                doDispatch(req, resp, chain);
            } else {
                noHandlerFound(req, resp);
            }
        } catch (Throwable ex) {
            /* spring mvc在这个地方是做了额外的异常处理的 */
            System.out.println("可以在这里再做一层异常处理，比如处理视图渲染方面的异常等，但现在什么都没做,异常消息是:" + ex.getMessage());
        } finally {
            /* 保存到ThreadLoca一定要清掉，所以放在finally是合理的 */
            context.clear();
        }
    }

    protected void doDispatch(HttpServletRequest req, HttpServletResponse resp, HandlerExecutionChain chain) throws Throwable {
        ViewResult viewResult;
        try {
            //这里返回false，直接return，结束后续流程
            if (!chain.applyPreHandle(req, resp)) {
                chain.applyPostHandle(req, resp);
                return;
            }

            viewResult = executeHandler(req, resp, chain.getHandler());

            chain.applyPostHandle(req, resp);
        } catch (Exception ex) {
            /*
                这里只处理Exception，非Exception并没有处理，会继续抛出给doService处理.
             */
            viewResult = resolveException(req, resp, chain.getHandler(), ex);
        }
        render(req, resp, viewResult);
    }

    protected ViewResult executeHandler(HttpServletRequest req, HttpServletResponse resp, Object handler) throws Exception {
        HandlerAdapter adapter = getHandlerAdapter(handler);
        return adapter.handle(req, resp, handler);
    }

    protected ViewResult resolveException(HttpServletRequest req, HttpServletResponse resp, Object handler, Exception ex) throws Exception {
        for (HandlerExceptionResolver exceptionResolver : exceptionResolvers) {
            Object result = exceptionResolver.resolveException(req, resp, handler, ex);
            if (result != null) {
                return (ViewResult) result;
            }
        }
        /*表示没有一个异常解析器可以处理异常，那么就应该把异常继续抛出,会交给doService方法去处理*/
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

    protected HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {
        for (HandlerMapping mapping : handlerMappings) {
            HandlerExecutionChain chain = mapping.getHandler(request);
            if (chain != null) {
                return chain;
            }
        }
        return null;
    }

    protected void noHandlerFound(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        /*利用default servlet来处理当前请求找不到handler的情况，默认servlet也可以处理静态资源，
        具体见spring mvc的DefaultServletHttpRequestHandler类*/
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

    /**
     * 参考spring 的DefaultCorsProcessor
     * <p>在预检请求下，才会设置的项
     * <ul>
     *     <li>setAccessControlMaxAge</li>
     *     <li>setAccessControlAllowHeaders</li>
     *     <li>setAccessControlAllowMethods</li>
     * </ul>
     * </p>
     *
     * <p>
     * 不管是不是预检请求都会设置的项有
     *     <ul>
     *         <li>setAccessControlAllowOrigin</li>
     *         <li>setAccessControlAllowCredentials</li>
     *     </ul>
     * </p>
     *
     * @param req
     * @param resp
     * @param configuration
     */
    protected void processCors(HttpServletRequest req, HttpServletResponse resp, CorsConfiguration configuration) {
        String requestOrigin = req.getHeader(HttpHeaders.ORIGIN);
        String allowOrigin = configuration.checkOrigin(requestOrigin);
        if (allowOrigin == null) {
            rejectRequest(resp);
            return;
        }
        //设置允许跨域请求的源
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowOrigin);
        resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, Boolean.toString(configuration.getAllowCredentials()));

        if (HttpMethod.OPTIONS.matches(req.getMethod())) {
            // 浏览器缓存预检请求结果时间,单位:秒
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, Long.toString(configuration.getMaxAge()));
            // 允许浏览器在预检请求成功之后发送的实际请求方法名，
            // 在MDN中只说要用逗号分隔即可，https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Access-Control-Allow-Methods
            // 但其举的例子是逗号后有一个空格，spring的HttpHeaders类的toCommaDelimitedString也是这样的
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, StringUtils.toCommaDelimitedString(configuration.getAllowedMethods(), ", "));
            // 允许浏览器发送的请求消息头
            resp.setHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, StringUtils.toCommaDelimitedString(configuration.getAllowedHeaders(), ", "));

        }
    }

    protected void rejectRequest(HttpServletResponse response)  {
        try {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getOutputStream().write("Invalid CORS request".getBytes(StandardCharsets.UTF_8));
            response.flushBuffer();
        } catch (IOException e) {
            throw new IllegalStateException("跨域处理失败");
        }
    }
    //endregion
}
