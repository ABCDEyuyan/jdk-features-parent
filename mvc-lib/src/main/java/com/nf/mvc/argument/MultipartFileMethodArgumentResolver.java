package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.file.StandardMultipartFile;
import com.nf.mvc.util.FileCopyUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultipartFileMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParamType();
        return isFileType(paramType)
                || (paramType.isArray() && isFileType(paramType.getComponentType()));
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
            handleSingleFile(part, fileType);
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

    private String getFileName(String disposition) {
        String fileName = disposition.substring(disposition.indexOf("filename=\"") + 10, disposition.lastIndexOf("\""));
        System.out.println(fileName);
        return fileName;
    }
}
