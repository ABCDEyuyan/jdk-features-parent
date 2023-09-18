package com.nf.mvc;

import com.nf.mvc.support.OrderComparator;
import com.nf.mvc.util.ReflectionUtils;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * mvc框架的上下文类，通过此类主要是获取只读的框架类型信息，此类的内容是在{@link DispatcherServlet}
 * 初始化的时候就已经获取并实例化完毕，之后就不在变动了，也就是说这里的这些类型都是单例的
 * <p>
 *     MvcContext类是一个单例实现，想获取其实例，调用{@link #getMvcContext()} 即可
 * </p>
 *
 * <p>
 *     此类有如下一些类型信息可以获取
 *     <ol>
 *         <li>扫描到的所有Class信息，这些类已经加载，但没有实例化，通过{@link #getAllScannedClasses()}获取</li>
 *         <li>获取mvc框架的核心组件，核心组件指的是如下几个,这些组件分为mvc框架内部提供的与用户提供的定制(custom)组件,这些组件分别用对用的getXxx方法来获取
 *              <ul>
 *                  <li>{@link HandlerMapping}</li>
 *                  <li>{@link HandlerAdapter}</li>
 *                  <li>{@link MethodArgumentResolver}</li>
 *                  <li>{@link HandlerExceptionResolver}</li>
 *              </ul>
 *         </li>
 *         <li>获取配置器，通过{@link #getCustomWebMvcConfigurer()}</li>
 *         <li>获取拦截器，通过{@link #getCustomHandlerInterceptors()} }</li>
 *     </ol>
 * </p>
 * @see DispatcherServlet
 */
public class MvcContext {

    private static final MvcContext INSTANCE = new MvcContext();

    private ScanResult scanResult;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();
    private List<Class<?>> allScannedClasses = new ArrayList<>();

    private List<HandlerMapping> customHandlerMappings = new ArrayList<>();
    private List<HandlerAdapter> customHandlerAdapters = new ArrayList<>();
    private List<MethodArgumentResolver> customArgumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> customExceptionResolvers = new ArrayList<>();
    private List<HandlerInterceptor> customInterceptors = new ArrayList<>();
    private List<WebMvcConfigurer> customConfigurers = new ArrayList<>();

    private MvcContext() {
    }

    public static MvcContext getMvcContext() {
        return INSTANCE;
    }

    /**
     * 设置成public修饰符，框架使用者是可以直接修改这个扫描结果,这样不安全，
     * 所以就改成默认修饰符，这样就只能在本包或子包中访问，基本上就是mvc框架内可以访问
     * <p>
     * 在整个mvc框架中，这扩展用的组件总是任何定制组件优先于默认组件，定制组件之间可以通过{@link com.nf.mvc.support.Order}注解调整顺序
     * </p>
     * <p>
     * 目前扫描的是各种各样的类，没有规定只扫描Handler，比如有HandlerMapping，也有HandlerAdapter以及Handler等
     * <p>
     * @param scanResult
     */
    void config(ScanResult scanResult) {
        this.scanResult = scanResult;
        ClassInfoList allClasses = scanResult.getAllClasses();
        for (ClassInfo classInfo : allClasses) {
            Class<?> scanedClass = classInfo.loadClass();
            // 下面的代码是测试用：与DispatcherServlet类加载器是同一个,见DispatcherServlet的init方法被注释的那行代码
            //System.out.println("scanedClass.getClassLoader() = " + scanedClass.getClassLoader());
            resolveMvcClass(scanedClass);
            allScannedClasses.add(scanedClass);
        }
    }

    /**
     * 解析扫描到的类是否是mvc框架扩展用的类，主要有4类接口的实现类+ViewResult子类归属于框架扩展类
     * @param scannedClass
     */
    private void resolveMvcClass(Class<?> scannedClass) {
        resolveClass(scannedClass, MethodArgumentResolver.class, customArgumentResolvers);
        resolveClass(scannedClass, HandlerMapping.class, customHandlerMappings);
        resolveClass(scannedClass, HandlerAdapter.class, customHandlerAdapters);
        resolveClass(scannedClass, HandlerExceptionResolver.class, customExceptionResolvers);
        resolveClass(scannedClass, HandlerInterceptor.class, customInterceptors);
        resolveClass(scannedClass, WebMvcConfigurer.class, customConfigurers);

    }

    private <T> void resolveClass(Class<?> scannedClass, Class<? extends T> mvcInf, List<T> list) {
        if (mvcInf.isAssignableFrom(scannedClass)) {
            T instance = (T) ReflectionUtils.newInstance(scannedClass);
            list.add(instance);
        }
    }

    public List<HandlerMapping> getCustomHandlerMappings() {
        Collections.sort(customHandlerMappings, new OrderComparator<>());
        return Collections.unmodifiableList(customHandlerMappings);
    }

    public List<HandlerAdapter> getCustomHandlerAdapters() {
        Collections.sort(customHandlerAdapters, new OrderComparator<>());
        return Collections.unmodifiableList(customHandlerAdapters);
    }

    public List<MethodArgumentResolver> getCustomArgumentResolvers() {
        Collections.sort(customArgumentResolvers, new OrderComparator<>());
        return Collections.unmodifiableList(customArgumentResolvers);
    }

    public List<HandlerExceptionResolver> getCustomExceptionResolvers() {
        Collections.sort(customExceptionResolvers, new OrderComparator<>());
        return Collections.unmodifiableList(customExceptionResolvers);
    }

    public List<HandlerInterceptor> getCustomHandlerInterceptors() {
        Collections.sort(customInterceptors, new OrderComparator<>());
        return Collections.unmodifiableList(customInterceptors);
    }

    public WebMvcConfigurer getCustomWebMvcConfigurer() {
        if (customConfigurers.size() >1) {
            throw new IllegalStateException("配置器应该只写一个");
        }
        return customConfigurers.size()==0?null:customConfigurers.get(0);
    }

    /**
     * 因为我们解析之后，结果就是固定的，如果直接返回List
     * 用户是可以更改集合里面的内容的，所以需要返回一个只读集合
     *
     * @return
     */
    public List<HandlerMapping> getHandlerMappings() {
        return Collections.unmodifiableList(handlerMappings);
    }

    public List<HandlerAdapter> getHandlerAdapters() {
        return Collections.unmodifiableList(handlerAdapters);
    }

    public List<MethodArgumentResolver> getArgumentResolvers() {
        return Collections.unmodifiableList(argumentResolvers);
    }

    public List<HandlerExceptionResolver> getExceptionResolvers() {
        return Collections.unmodifiableList(exceptionResolvers);
    }

    public List<Class<?>> getAllScannedClasses() {
        return Collections.unmodifiableList(allScannedClasses);
    }

    /**
     * 以下这些方法是默认修饰符，主要是在框架内调用，用户不能调用
     * @return
     */
    ScanResult getScanResult() {
        return this.scanResult;
    }

    void setHandlerMappings(List<HandlerMapping> handlerMappings) {
        this.handlerMappings = handlerMappings;
    }

    void setHandlerAdapters(List<HandlerAdapter> handlerAdapters) {
        this.handlerAdapters = handlerAdapters;
    }

    void setArgumentResolvers(List<MethodArgumentResolver> argumentResolvers) {
        this.argumentResolvers = argumentResolvers;
    }

    void setExceptionResolvers(List<HandlerExceptionResolver> exceptionResolvers) {
        this.exceptionResolvers = exceptionResolvers;
    }

}
