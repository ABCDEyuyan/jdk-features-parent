package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 此类主要是用来响应流，比如给一个图片的url，直接响应图片的字节数据，以便一个图片能正确显示，比如
 * <pre class="code">
 *     <img src="http://localhost:8080/product/a.jpg"/>
 * </pre>
 * TODO:注意：此类不仅仅是为了图片显示设计的，还可以有其它功能
 */
public class StreamViewResult extends ViewResult {
    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws Exception {

    }
}

/*
private InputStream inputStream;
    private Map<String, String> headers;
    private int bufferSize = 2048;

    public SteamView(InputStream inputStream, Map<String, String> headers) {
        this.inputStream = inputStream;
        this.headers = headers;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }

    @Override
    protected void render(WebParam param) throws ServletException, IOException {
        HttpServletResponse response = param.getResponse();
        response.setContentType("application/octet-stream");
        headers.forEach((name, value) -> response.setHeader(name, value));
        try(BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            OutputStream os = response.getOutputStream();
            int len = 0;
            byte[] bytes = new byte[bufferSize];
            while((len = bis.read(bytes, 0, bytes.length)) != -1){
                os.write(bytes, 0, len);
            }
        }
    }

 */