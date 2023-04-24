package mvcdemo.controller;

import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.FileViewResult;

import static com.nf.mvc.handler.HandlerHelper.file;

public class FileController {
    @RequestMapping("/download")
    public FileViewResult download(String filename) {
        String realPath ="e:/Image/"+filename;
        return file(realPath);
    }
}
