package com.nf.demo.web.controller;

import com.nf.demo.util.MinioUtils;
import com.nf.mvc.ViewResult;
import com.nf.mvc.argument.RequestParam;
import com.nf.mvc.mapping.RequestMapping;

import java.io.InputStream;

import static com.nf.mvc.handler.HandlerHelper.file;

@RequestMapping("/minio")
public class MinioFileController {

    @RequestMapping("/download")
    public ViewResult download(@RequestParam(defaultValue = "firstbuckets") String bucket, @RequestParam(defaultValue = "1.jpg") String filename) {
        InputStream inputStream = MinioUtils.downloadStream(bucket, filename);
        return file(inputStream,filename);

    }
}
