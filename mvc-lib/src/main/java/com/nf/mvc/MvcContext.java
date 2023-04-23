package com.nf.mvc;

import com.nf.mvc.support.OrderComparator;
import com.nf.mvc.util.ReflectionUtils;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MvcContext {

    private final OrderComparator orderComparator = new OrderComparator();
    private static final MvcContext instance = new MvcContext();

    private ScanResult scanResult;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();
    private List<Class<?>> allScanedClasses = new ArrayList<>();

    private List<HandlerMapping> customHandlerMappings = new ArrayList<>();
    private List<HandlerAdapter> customHandlerAdapters = new ArrayList<>();
    private List<MethodArgumentResolver> customArgumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> customExceptionResolvers = new ArrayList<>();

    private MvcContext() {
    }

    public static MvcContext getMvcContext() {
        return instance;
    }

    /**
     * 设置成public修饰符，框架使用者是可以直接修改这个扫描结果
     * 影响就不是很好，所以就改成default修饰符，只能在本包
     * 或子包中访问，基本上就是mvc框架内可以访问
     * <p>
     * 我设计的mvc框架，是有以下几个扩展点
     * DispatcherServlet
     * HandlerMapping
     * HandlerAdapter
     * MethodArgumentResolver
     * HandlerExceptionResolver
     * ViewResult
     * <p>
     * 但只有HandlerMapping、HandlerAdapter、MethodArgumentResolver、HandlerExceptionResolver
     * 这四个接口是用通过扫描的形式获取的。
     * <p>
     * 在整个mvc框架中，这4个组件分为用户提供的定制组件或mvc框架提供的默认组件，定制组件可以通过Order注解调整顺序
     * 默认组件直接由作者在框架源代码中调整顺序即可
     * <p>
     * 目前扫描的是各种各样的类，没有规定只扫描Handler，
     * 比如有HandlerMapping，也有HandlerAdapter以及Handler等
     * <p>
     * //与DispatcherServlet类加载器是同一个
     * //System.out.println("scanedClass.getClassLoader() = " + scanedClass.getClassLoader());
     *
     * @param scanResult
     */
    void config(ScanResult scanResult) {

        this.scanResult = scanResult;
        ClassInfoList allClasses = scanResult.getAllClasses();
        for (ClassInfo classInfo : allClasses) {
            Class<?> scanedClass = classInfo.loadClass();
            resolveMvcClass(scanedClass);
            allScanedClasses.add(scanedClass);
        }
    }

    /**
     * 解析类是否是mvc框架扩展用的类，主要有4类接口的实现类归属于框架扩展类
     *
     * @param scanedClass
     */
    private void resolveMvcClass(Class<?> scanedClass) {
        resolveClass(scanedClass, HandlerMapping.class, customHandlerMappings);
        resolveClass(scanedClass, HandlerAdapter.class, customHandlerAdapters);
        resolveClass(scanedClass, MethodArgumentResolver.class, customArgumentResolvers);
        resolveClass(scanedClass, HandlerExceptionResolver.class, customExceptionResolvers);

    }

    private <T> void resolveClass(Class<?> scannedClass, Class<? extends T> mvcInf, List<T> list) {
        if (mvcInf.isAssignableFrom(scannedClass)) {
            T instance = (T) ReflectionUtils.newInstance(scannedClass);
            list.add(instance);
        }
    }

    ScanResult getScanResult() {
        return this.scanResult;
    }

    public List<HandlerMapping> getCustomHandlerMappings() {
        Collections.sort(customHandlerMappings, orderComparator);
        return Collections.unmodifiableList(customHandlerMappings);
    }

    public List<HandlerAdapter> getCustomHandlerAdapters() {
        Collections.sort(customHandlerAdapters, orderComparator);
        return Collections.unmodifiableList(customHandlerAdapters);
    }

    public List<MethodArgumentResolver> getCustomArgumentResolvers() {
        Collections.sort(customArgumentResolvers, orderComparator);
        return Collections.unmodifiableList(customArgumentResolvers);
    }

    public List<HandlerExceptionResolver> getCustomExceptionResolvers() {
        Collections.sort(customExceptionResolvers, orderComparator);
        return Collections.unmodifiableList(customExceptionResolvers);
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
    // 以下四个方法是默认修饰符，主要是在框架内调用，用户不能调用
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

    public List<Class<?>> getAllScanedClasses() {
        return Collections.unmodifiableList(allScanedClasses);
    }
}