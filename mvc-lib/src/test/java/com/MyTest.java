package com;

import com.nf.mvc.exception.ExceptionHandler;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.support.path.AntPathMatcher;
import com.nf.mvc.util.AnnotationUtils;
import com.nf.mvc.util.JacksonUtils;
import org.junit.Test;

import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

public class MyTest {
  private long counter;

  private void wasCalled() {
    counter++;
  }

  @Test
  public void ss() {
    final List<String> list = new ArrayList<>();
    list.add("a");
    list.add("a");
    list.add("b");
    list.add("c");
    List<String> list1 = distinct(list);
    System.out.println("list1.hashCode() = " + list1.hashCode());
    System.out.println("list.hashCode() = " + list.hashCode());
    list1.forEach(System.out::println);
  }
  private List<String> distinct(List<String> values) {
    return values.stream().distinct().collect(Collectors.toList());
  }
  @Test
  public void ss2() {
    List<String> list = Arrays.asList("abc1", "abc2", "abc3");
    counter = 0;
    list.stream()
            .filter(element -> {
              System.out.println("filter() was called");
              // return element.contains("2");
              return true;
            })
            .map(element -> {
              System.out.println("map() was called");
              return element.toUpperCase();
            })
            .limit(4)
            .collect(Collectors.toList());
  }

  @Test
  public void ss3() {

    String pattern = "/list/{pageno}/{pagesize}/abc";
    String path = "/list/2/5";
    //  String p2 = pattern.replaceAll("\\{.*?\\}", "*");
    //  System.out.println("p2 = " + p2);
    //  System.out.println("pattern = " + pattern);
    AntPathMatcher pathMatcher = new AntPathMatcher.Builder().build();
    System.out.println(pathMatcher.isMatch(pattern, path));
    Map<String, String> variables = pathMatcher.extractPathVariables(pattern, path);
    variables.forEach((k, v) -> {
      System.out.println("key:" + k);
      System.out.println("value:" + v);
    });
  }

  @Test
  public void s4() {
    AntPathMatcher pathMatcher = new AntPathMatcher.Builder().build();
    String pattern = "a/*/c/d";
    String path = "a/b0/c";

    boolean match = pathMatcher.isMatch(pattern, path);
    System.out.println("match = " + match);

    AntPathMatcher pathMatcher2 = new AntPathMatcher.Builder().withMatchStart()
            .build();
    boolean matchStart = pathMatcher2.isMatch(pattern, path);
    System.out.println("matchStart = " + matchStart);

  }

  @Test
  public void s5() {
    Map<Integer, String> map = new HashMap<>();
    map.put(1, "a");
    map.put(2, "b");
    System.out.println(JacksonUtils.toJson(map));
  }

  @Test
  public void s6() throws Exception {
    Method me = A.class.getDeclaredMethod("test");
    String value = AnnotationUtils.getAttrValue(me, RequestMapping.class);
    System.out.println("value = " + value);
  }


  static class A {
    @ExceptionHandler(ArithmeticException.class)
    @RequestMapping("/a/b/c")
    public void test() {

    }
  }
}
