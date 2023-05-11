package com.nf.mvc.handler;

import com.nf.mvc.ViewResult;
import com.nf.mvc.util.StreamUtils;
import com.nf.mvc.view.*;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class HandlerHelper {
    public static VoidViewResult empty() {
        return new VoidViewResult();
    }

    public static JsonViewResult json(Object obj) {
        return new JsonViewResult(obj);
    }

    public static PlainViewResult plain(String text) {
        return new PlainViewResult(text);
    }

    public static HtmlViewResult html(String html) {
        return new HtmlViewResult(html);
    }

    public static ForwardViewResult forward(String url) {
        return new ForwardViewResult(url);
    }

    public static ForwardViewResult forward(String url,Map<String, Object> model) {
        return new ForwardViewResult(url,model);
    }

    public static RedirectViewResult redirect(String url) {
        return new RedirectViewResult(url);
    }

    public static RedirectViewResult redirect(String url,Map<String,String> model) {
        return new RedirectViewResult(url,model);
    }

    public static FileViewResult file(String realPath) {
        return new FileViewResult(realPath);
    }

    public static StreamViewResult stream(InputStream inputStream) {
        return new StreamViewResult(inputStream);
    }
    public static StreamViewResult stream(InputStream inputStream, Map<String,String> headers) {
        return new StreamViewResult(inputStream,headers);
    }

    public static StreamViewResult stream(String realPath) {
        return stream(realPath, new HashMap<>());
    }
    public static StreamViewResult stream(String realPath, Map<String,String> headers) {
        return new StreamViewResult(StreamUtils.getInputStreamFromRealPath(realPath),headers);
    }
}
