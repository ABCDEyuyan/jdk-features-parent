package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.file.StandardMultipartFile;
import com.nf.mvc.util.FileCopyUtils;
import com.nf.mvc.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultipartFileMethodArgumentResolver implements MethodArgumentResolver {
    /**
     * TODO:改进到也支持List
     * @param parameter
     * @return
     */
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParamType();
        return isFileType(paramType)
                || (paramType.isArray() && isFileType(paramType.getComponentType()));
              //  || ReflectionUtils.isListOrSet(paramType) && isFileType();
    }

    private boolean isFileType(Class<?> fileType) {
        return Part.class == fileType ||
                MultipartFile.class == fileType;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        Class<?> paramType = parameter.getParamType();
        String paramName = parameter.getParamName();
        if (paramType.isArray()) {
            return handleMultiFile(request.getParts(), paramType.getComponentType());
        } else {
            return handleSingleFile(request.getPart(paramName), paramType);
        }

    }

    private List<?> handleMultiFile(Collection<Part> parts, Class<?> fileType) {
        List<?> files = new ArrayList<>();
        for (Part part : parts) {
            Object uploaded = handleSingleFile(part, fileType);
           // files.add(uploaded);
        }
        return files;
    }

    private Object handleSingleFile(Part part, Class<?> paramType) {
        if (Part.class == paramType) {
            return part;
        } else {
            String disposition = part.getHeader(FileCopyUtils.CONTENT_DISPOSITION);
            String filename = getFileName(disposition);
            return new StandardMultipartFile(part, filename);
        }
    }

    /**
     * TODO:获取文件名的代码可以再改进一下
     * @param disposition
     * @return
     */
    private String getFileName(String disposition) {
        String fileName = disposition.substring(disposition.indexOf("filename=\"") + 10, disposition.lastIndexOf("\""));
        System.out.println(fileName);
        return fileName;
    }
}
