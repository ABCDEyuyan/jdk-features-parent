package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileInputStream;

public class FileViewResult extends ViewResult {
    /**
     * realPath指的文件的物理路径
     */
    private String realPath;
    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        //TODO:返回内容类型，文件名乱码，文件名
        ServletOutputStream outputStream = resp.getOutputStream();
        resp.setHeader("Content-disposition", "attachment; filename=sample.txt");

        FileInputStream inputStream = new FileInputStream(realPath);
        byte[] buffer = new byte[1024];
        int len = -1;
        while ( ( len = inputStream.read(buffer))!= -1){
            outputStream.write(buffer,0,len);
        }
        outputStream.flush();
    }
}
