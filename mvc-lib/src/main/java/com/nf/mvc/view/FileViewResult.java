package com.nf.mvc.view;

import com.nf.mvc.ViewResult;
import com.nf.mvc.util.FileCopyUtils;
import com.nf.mvc.util.StreamUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileViewResult extends StreamViewResult {

    /**
     * realPath指的文件的物理路径,比如D:/downloads/1.jpg
     */
    private String realPath;
    private String filename;

    public FileViewResult(String realPath) {
        this(realPath, new HashMap<>());
    }

    public FileViewResult(String realPath, Map<String,String> headers) {
        super(StreamUtils.getInputStreamFromRealPath(realPath));
        this.realPath = realPath;
        this.filename = getFileName();
    }

    @Override
    protected void writeContentType(HttpServletResponse resp) throws Exception {
        resp.setContentType(getMediaType(this.filename));
    }

    @Override
    protected void writeHeaders(HttpServletResponse resp) throws Exception {
        //attachment表示以附件的形式下载，对文件名编码以防止中文文件名在保存对话框中是乱码的
        resp.setHeader("Content-disposition", "attachment; filename=" + URLEncoder.encode(filename, "UTF-8"));
        super.writeHeaders(resp);
    }

    protected String getFileName() {
        int lastSlash = realPath.lastIndexOf("/");
        return realPath.substring(lastSlash + 1);
    }

    protected String getMediaType(String filename) {
        //guessContentTypeFromName是从文件名猜测其内容类型，如果为null就猜测失败
        String midiaType = URLConnection.guessContentTypeFromName(filename);
        if (midiaType == null) {
            midiaType = StreamUtils.APPLICATION_OCTET_STREAM_VALUE;
        }
        return midiaType;
    }
}

