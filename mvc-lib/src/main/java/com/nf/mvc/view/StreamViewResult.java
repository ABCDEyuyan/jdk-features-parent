package com.nf.mvc.view;

import com.nf.mvc.ViewResult;
import com.nf.mvc.util.ObjectUtils;
import com.nf.mvc.util.StreamUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 此类主要是用来响应流，比如给一个图片的url，直接响应图片的字节数据，以便一个图片能正确显示，比如
 * <pre class="code">
 *     <img src="http://localhost:8080/product/a.jpg"/>
 * </pre>
 * TODO:注意：此类不仅仅是为了图片显示设计的，还可以有其它功能
 */
public class StreamViewResult extends ViewResult {
    private  Map<String, String> headers ;
    private InputStream inputStream;

    public StreamViewResult(InputStream inputStream) {
        this(inputStream, new HashMap<>());
    }

    public StreamViewResult(InputStream inputStream, Map<String, String> headers) {
        this.inputStream = inputStream;
        this.headers = headers;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        writeContentType(resp);
        writeHeaders(resp);
        writeContent(resp);
    }

    protected void writeContentType(HttpServletResponse resp) throws Exception{
        resp.setContentType(StreamUtils.APPLICATION_OCTET_STREAM_VALUE);
    }

    protected void writeHeaders(HttpServletResponse resp) throws Exception {
        if (ObjectUtils.isEmpty(headers)) {
            return;
        }
        headers.forEach((k,v)-> resp.setHeader(k,v));
    }

    protected void writeContent(HttpServletResponse resp) throws Exception {
        try (InputStream input = this.inputStream; OutputStream output = resp.getOutputStream()) {
            StreamUtils.copy(input, output);
        }
    }

}
