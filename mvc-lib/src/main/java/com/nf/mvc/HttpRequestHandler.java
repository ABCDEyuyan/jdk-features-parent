package com.nf.mvc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface HttpRequestHandler {
    void processRequest(HttpServletRequest req, HttpServletResponse response) throws Exception;
}
