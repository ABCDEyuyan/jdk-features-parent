package mvcdemo.web.controller;

import com.nf.mvc.HandlerContext;
import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.FileViewResult;
import com.nf.mvc.view.JsonViewResult;
import com.nf.mvc.view.StreamViewResult;

import javax.servlet.http.Part;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static com.nf.mvc.handler.HandlerHelper.*;

public class FileController {
    @RequestMapping("/download")
    public FileViewResult download(String filename) {
        String realPath ="e:/Image/"+filename;
        return file(realPath);
    }

    @RequestMapping("/stream")
    public StreamViewResult streamDownload(String filename) {
        String realPath ="e:/Image/"+filename;
        return stream(realPath);
    }
    //一定要对Dispatcherservlet设置multipart config
    //方法参数是Part或者MultipartFile

    @RequestMapping("/upload")
    public String upload(MultipartFile multipartFile, Part part) throws Exception {
        System.out.println("=============简单文件上传测试==============");
        if (multipartFile == null || part == null) {
            return "kong";
        }
        System.out.println("multipartFile.getName() = " + multipartFile.getName());
        System.out.println("part.getName() = " + part.getName());
        System.out.println("multipartFile.getOriginalFilename() = " + multipartFile.getOriginalFilename());
        System.out.println("part.getSubmittedFileName() = " + part.getSubmittedFileName());

        String uploadedFilename = multipartFile.getOriginalFilename();
        Path path = Paths.get("D:/tmp", uploadedFilename);
        multipartFile.transferTo(path);


        return "ok";
    }

    @RequestMapping("/upload2")
    public String upload(MultipartFile[] multipartFile, List<Part> parts) throws Exception {
        System.out.println("=============复杂文件上传测试==============");
        for (MultipartFile file : multipartFile) {
            String uploadedFilename = file.getOriginalFilename();
            Path path = Paths.get("D:/tmp", uploadedFilename);
            file.transferTo(path);
        }
        System.out.println(parts.size());
        return "ok";
    }
}
