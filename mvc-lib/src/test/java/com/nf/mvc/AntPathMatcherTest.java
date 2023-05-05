package com.nf.mvc;

import com.nf.mvc.support.AntPathMatcher;
import com.nf.mvc.support.MultiValueMapAdapter;
import org.checkerframework.checker.units.qual.K;
import org.junit.Test;

import java.util.*;

/**
 * https://blog.csdn.net/justry_deng/article/details/123641133
 */
public class AntPathMatcherTest {
    @Test
    public void testHello(){
        AntPathMatcher.Builder builder = new AntPathMatcher.Builder();
        AntPathMatcher pathMatcher = builder
                .withMatchStart().withIgnoreCase().build();

        System.out.println("pathMatcher.isMatch(\"/abc/*\",\"/abc/def\") = " + pathMatcher.isMatch("/abc/*", "/abc/def"));

        System.out.println("pathMatcher.isMatch(\"/abc/t?d.jsp\",\"/abc/tad.jsp\") = " + pathMatcher.isMatch("/abc/t?d.jsp", "/abc/tad.jsp"));
        System.out.println("pathMatcher.isMatch(\"/t?d.jsp\",\"/tad.jsp\") = " + pathMatcher.isMatch("/t?d.jsp", "/tad.jsp"));
        System.out.println("pathMatcher.isMatch(\"/t?d.jsp\",\"tadas.jsp\") = " + pathMatcher.isMatch("/t?d.jsp", "tadas.jsp"));
        System.out.println("pathMatcher.isMatch(\"/mvc/**/index.jsp\",\"mvc/a/b/index.jsp\") = " + pathMatcher.isMatch("/mvc/**/index.jsp", "mvc/a/b/index.jsp"));
        System.out.println("pathMatcher.isMatch(\"/mvc/**/index.jsp\",\"/mvc/index.jsp\") = " + pathMatcher.isMatch("/mvc/**/index.jsp", "/mvc/index.jsp"));
        System.out.println("pathMatcher.isMatch(\"/mvc/**/index.jsp\",\"/mvc/a/index.jsp\") = " + pathMatcher.isMatch("/mvc/**/index.jsp", "/mvc/a/index.jsp"));
        System.out.println("pathMatcher.isMatch(\"/mvc/abc\", \"/mvc/abc\") = " + pathMatcher.isMatch("/mvc/abc", "/mvc/abc"));

    }

    @Test
    public void testMatchStart(){
        AntPathMatcher.Builder builder = new AntPathMatcher.Builder();
        AntPathMatcher pathMatcher = builder
                .withMatchStart()
                .withIgnoreCase()
                .build();

        AntPathMatcher.Builder builder2 = new AntPathMatcher.Builder();
        AntPathMatcher pathMatcher2 = builder2
             .withIgnoreCase().build();
        //如果path与patter完全匹配，那么肯定是算匹配的，比如a/*/c模式与a/b0/c
        //但如果在设置了matchStart的情况下，pattern的前面一部分就已经匹配了整个path的话，也算匹配，比如a/*/c/d模式与a/b0/c路径
        System.out.println("=============match start= true");
        System.out.println(pathMatcher.isMatch("a/*", "a/b0/c")); //false
        System.out.println(pathMatcher.isMatch("a/*/c", "a/b0/c")); //true
        System.out.println(pathMatcher.isMatch("a/*/c/d", "a/b0/c"));//true

        System.out.println("=============match start= false");
        System.out.println(pathMatcher2.isMatch("a/*", "a/b0/c"));//false
        System.out.println(pathMatcher2.isMatch("a/*/c", "a/b0/c"));//true
        System.out.println(pathMatcher2.isMatch("a/*/c/d", "a/b0/c"));//false
    }

    @Test
    public void testAntComparator(){
        AntPathMatcher pathMatcher = new AntPathMatcher.Builder().build();
        Comparator<String> patternComparator = pathMatcher.getPatternComparator("/insert/a");
        Map<String, Integer> handlers = new HashMap<>();
        handlers.put("/**", 1);
        handlers.put("/*", 2);
        handlers.put("/insert/**", 3);
        handlers.put("/insert/*", 4);
        handlers.put("/insert/a", 5);
        handlers.put("/insert/b", 6);

        TreeMap<String, Integer> treeMap = new TreeMap( patternComparator);
        treeMap.putAll(handlers);
        treeMap.entrySet().forEach(System.out::println);
        System.out.println("===================");
        handlers.entrySet().forEach(System.out::println);


    }
}
