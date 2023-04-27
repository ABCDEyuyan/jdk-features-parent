package mvcdemo.controller;

import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.FileViewResult;
import com.nf.mvc.view.JsonViewResult;

import javax.servlet.http.Part;

import java.nio.file.Path;
import java.nio.file.Paths;

import static com.nf.mvc.handler.HandlerHelper.file;
import static com.nf.mvc.handler.HandlerHelper.json;

public class FileController {
    @RequestMapping("/download")
    public FileViewResult download(String filename) {
        String realPath ="e:/Image/"+filename;
        return file(realPath);
    }


    //一定要对Dispatcherservlet设置multipart config
    @RequestMapping("/upload")
    public String upload(MultipartFile multipartFile, Part file) throws Exception {
        String uploadedFilename = multipartFile.getOriginalFilename();
        Path path = Paths.get("D:/tmp", uploadedFilename);
        multipartFile.transferTo(path);
        return "ok";
    }
}
