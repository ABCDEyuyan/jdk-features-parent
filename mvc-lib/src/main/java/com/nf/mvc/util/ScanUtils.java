package com.nf.mvc.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public class ScanUtils {

    public static ClassInfoList scan(String packageName) {
        ClassGraph classGraph = new ClassGraph();
        classGraph.enableAllInfo();
        classGraph.acceptPackages(packageName);
        ScanResult scanResult = classGraph.scan();
        return scanResult.getAllClasses();

    }
}
