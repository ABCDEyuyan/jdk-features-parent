package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Parameter;

public interface MethodArgumentResolver {
    boolean supports(Parameter parameter);

    Object resolveArgument(Parameter parameter, HttpServletRequest request) throws Exception;
}
