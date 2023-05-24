package com.nf.demo.web.controller;

import com.nf.mvc.ViewResult;
import com.nf.mvc.mapping.RequestMapping;

import static com.nf.mvc.handler.HandlerHelper.file;

@RequestMapping("/file")
public class FileController {

    @RequestMapping("/download")
    public ViewResult download(String filename) {
        String realPath = "C:/Users/27423/Desktop/"+filename;
        return file(realPath);

    }
}
