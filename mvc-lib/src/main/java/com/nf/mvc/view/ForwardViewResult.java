package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ForwardViewResult extends ViewResult {
    private String url;

    public ForwardViewResult(String url) {
        this.url = url;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        req.getRequestDispatcher(url).forward(req,resp);
    }
}
