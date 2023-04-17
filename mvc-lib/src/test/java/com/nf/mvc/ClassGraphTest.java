package com.nf.mvc;

import io.github.classgraph.*;
import org.junit.Test;

import java.lang.annotation.Annotation;

public class ClassGraphTest {

    @Test
    public void hello(){
        // 如果出现错误提示，让你enableXX的时候，你可以简单的调用enableAllInfo方法即可
        //1：创建一个ClassGraph（graph：图）对象
        ClassGraph classGraph = new ClassGraph();
        //classGraph.acceptPackages(getClass().getPackage().getName());
        //2.设置要扫描的包
        classGraph.acceptPackages("com.nf.mvc");
        //3.开始扫描
        ScanResult scanResult = classGraph.scan();

        //分析使用扫描结果
        ClassInfoList allClasses = scanResult.getAllClasses();

        for (ClassInfo allClass : allClasses) {
            System.out.println(allClass.getSimpleName());
        }



    }

    @Test
    public void testScanMethodHaveAnnoClass(){
        ClassGraph classGraph = new ClassGraph();
        classGraph.enableAllInfo();
        classGraph.acceptPackages("com.nf.mvc");

        ScanResult result = classGraph.scan();
        ClassInfoList classInfoList = result.getClassesWithMethodAnnotation(RequestMapping.class);

        for (ClassInfo allClass : classInfoList) {
            System.out.println(allClass.getName());
        }

    }


    @Test
    public void testScanMethodHaveAnnoValueClass(){
        ClassGraph classGraph = new ClassGraph();
        classGraph.enableAllInfo();
        classGraph.acceptPackages("com.nf.mvc");

        ScanResult result = classGraph.scan();
        ClassInfoList classInfoList = result.getClassesWithMethodAnnotation(RequestMapping.class);

        for (ClassInfo clz : classInfoList) {
            MethodInfoList methodInfoList = clz.getDeclaredMethodInfo();

            for (MethodInfo methodInfo : methodInfoList) {
                if (methodInfo.hasAnnotation(RequestMapping.class)) {
                    AnnotationInfo annotationInfo = methodInfo.getAnnotationInfo(RequestMapping.class);
                  String value = annotationInfo.getParameterValues().getValue("value").toString();

                    if (value.equals("web")) {
                        System.out.println("methodInfo.getName() = " + methodInfo.getName());
                        System.out.println("clz.getSimpleName() = " + clz.getSimpleName());
                    }
                }
            }
        }

    }
}
