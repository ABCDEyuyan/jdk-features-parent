package com.nf.mvc.argument;

import java.lang.reflect.Parameter;

public class MethodParameter {
    private Parameter parameter;
    private String paramName;

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
