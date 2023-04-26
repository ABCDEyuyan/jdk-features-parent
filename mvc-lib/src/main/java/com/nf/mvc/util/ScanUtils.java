package com.nf.mvc.util;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;

public interface ScanUtils {

    public static ScanResult scan(String packageName) {
        ClassGraph classGraph = new ClassGraph();
        classGraph.enableAllInfo();
        classGraph.acceptPackages(packageName);
        ScanResult scanResult = classGraph.scan();
        return scanResult;

    }
}
