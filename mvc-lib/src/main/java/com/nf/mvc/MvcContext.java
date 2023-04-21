package com.nf.mvc;

import com.nf.mvc.util.ReflectionUtils;
import io.github.classgraph.ClassInfo;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MvcContext {

    private static final MvcContext instance = new MvcContext();

    private ScanResult scanResult;

    private List<HandlerMapping> handlerMappings = new ArrayList<>();
    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();
    private List<MethodArgumentResolver> argumentResolvers = new ArrayList<>();
    private List<HandlerExceptionResolver> exceptionResolvers = new ArrayList<>();

    private List<Class<?>> allScanedClasses = new ArrayList<>();

    private MvcContext(){}


    public static MvcContext getMvcContext(){
        return instance;
    }

    /**
     * 设置成public修饰符，框架使用者是可以直接修改这个扫描
     * 影响就不是很好，所以就改成default修饰符，只能在本包
     * 或子包中访问，基本上就是mvc框架内可以访问
     *
     * 我设计的mvc框架，是有以下几个扩展点
     * DispatcherServlet
     * HandlerMapping
     * HandlerAdapter
     * MethodArgumentResolver
     * HandlerExceptionResolver
     * ViewResult
     *
     * 目前扫描的是各种各样的类，没有规定只扫描Handler，
     * 比如有HandlerMapping，也有HandlerAdapter以及Handler等
     *
     *  //与DispatcherServlet类加载器是同一个
     *  //System.out.println("scanedClass.getClassLoader() = " + scanedClass.getClassLoader());
     * @param scanResult
     */
     void config(ScanResult scanResult) {

        this.scanResult =scanResult;
        ClassInfoList allClasses = scanResult.getAllClasses();
        for (ClassInfo classInfo : allClasses) {
            Class<?> scanedClass = classInfo.loadClass();
            resolveMvcClass(scanedClass);
            allScanedClasses.add(scanedClass);
        }
    }

    /**
     * 解析类是否是mvc框架扩展用的类，主要有4类接口的实现类归属于框架扩展类
     * @param scanedClass
     */
    private void resolveMvcClass(Class<?> scanedClass){
        resolveClasses(HandlerMapping.class,scanedClass,handlerMappings);
        resolveClasses(HandlerAdapter.class,scanedClass,handlerAdapters);
        resolveClasses(MethodArgumentResolver.class,scanedClass,argumentResolvers);
        resolveClasses(HandlerExceptionResolver.class,scanedClass,exceptionResolvers);

    }

    private <T> void resolveClasses(Class<? extends T> mvcInf, Class<?> scannedClass,List<T> list) {
        if (mvcInf.isAssignableFrom(scannedClass)) {
            T instance = (T)ReflectionUtils.newInstance(scannedClass);
            list.add(instance);
        }
    }
     ScanResult getScanResult(){
        return  this.scanResult;
    }

    /**
     * 因为我们解析之后，结果就是固定的，如果直接返回List
     * 用户是可以更改集合里面的内容的，所以需要返回一个只读集合
     * @return
     */
    public List<HandlerMapping> getHandlerMappings() {
        return Collections.unmodifiableList(handlerMappings);
    }

    public List<HandlerAdapter> getHandlerAdapters(){
        return Collections.unmodifiableList(handlerAdapters);
    }

    public List<MethodArgumentResolver> getArgumentResolvers(){
        return Collections.unmodifiableList(argumentResolvers);
    }

    public List<HandlerExceptionResolver> getExceptionResolvers(){
        return Collections.unmodifiableList(exceptionResolvers);
    }

    public List<Class<?>> getAllScanedClasses(){
        return Collections.unmodifiableList(allScanedClasses);
    }
}
