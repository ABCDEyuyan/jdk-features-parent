package com.nf.mvc;

import com.nf.mvc.argument.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public interface MethodArgumentResolver {

    boolean supports(MethodParameter parameter);

    Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception;
}
