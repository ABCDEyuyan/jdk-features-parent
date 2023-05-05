package com.nf.mvc.argument;

import com.nf.mvc.util.ObjectUtils;

import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

public class MethodParameter {
    private final Method method;

    private Parameter parameter;
    private String paramName;
    private int parameterIndex;
    private Class<?> containingClass;

    public MethodParameter(Method method, int parameterIndex,String paramName) {
        this(method, parameterIndex, paramName,method.getDeclaringClass());
    }

    public MethodParameter(Method method, int parameterIndex, String paramName,Class<?> containingClass) {
        this.method = method;
        this.parameterIndex = parameterIndex;
        this.paramName = paramName;
        this.containingClass = containingClass;

    }

    public Class<?> getParamType() {
        return getParameter().getType();
    }

    public Parameter getParameter() {
        if (this.parameterIndex < 0) {
            throw new IllegalStateException("Cannot retrieve Parameter descriptor for method return type");
        }
        Parameter parameter = this.parameter;
        if (parameter == null) {
            parameter = method.getParameters()[this.parameterIndex];
            this.parameter = parameter;
        }
        return parameter;
    }

    public String getParamName() {
        return paramName;
    }

    public Method getMethod() {
        return method;
    }

    public int getParameterIndex() {
        return parameterIndex;
    }

    public Class<?> getContainingClass() {
        return containingClass;
    }

    @Override
    public boolean equals( Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof MethodParameter)) {
            return false;
        }
        MethodParameter otherParam = (MethodParameter) other;
        return (getContainingClass() == otherParam.getContainingClass()  &&
                this.parameterIndex == otherParam.parameterIndex &&
                this.getMethod().equals(otherParam.getMethod()));
    }

    @Override
    public int hashCode() {
        return (31 * this.getMethod().hashCode() + this.parameterIndex);
    }
}
