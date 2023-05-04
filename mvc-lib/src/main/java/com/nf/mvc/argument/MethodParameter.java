package com.nf.mvc.argument;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {
    //private final Method method;

    private Parameter parameter;
    private String paramName;
    private int parameterIndex;
    private Class<?> containingClass;
    /*
      public MethodParameter(Method method, int parameterIndex) {
          this(method, parameterIndex, method.getDeclaringClass());
      }

          public MethodParameter(Method method, int parameterIndex,Class<?> containingClass) {
              this.method = method;
              this.parameterIndex = parameterIndex;
              this.containingClass = containingClass;
          }

          public MethodParameter(Parameter parameter, String paramName) {
              this.parameter = parameter;
              this.paramName = paramName;
          }*/
    public MethodParameter() {
    }

    public MethodParameter(Parameter parameter, String paramName) {
        this.parameter = parameter;
        this.paramName = paramName;
    }

    public Class<?> getParamType() {
        return parameter.getType();
    }

    public Parameter getParameter() {
        return parameter;
    }

    public String getParamName() {
        return paramName;
    }
}
