package com.nf.mvc;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 此接口用来依据请求找到对应的处理者（Handler)
 */
public interface HandlerMapping {
    /**
     * TODO：相比ServletException，IOException还是觉得抛出Exception异常更合理
     * @param request
     * @return 是一个请求处理者（或者执行链）
     * @throws Exception
     */
    Object getHandler(HttpServletRequest request) throws ServletException, IOException;
}
