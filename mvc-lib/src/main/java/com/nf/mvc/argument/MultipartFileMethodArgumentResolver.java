package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.file.StandardMultipartFile;
import com.nf.mvc.util.FileCopyUtils;
import com.nf.mvc.util.ObjectUtils;
import com.nf.mvc.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MultipartFileMethodArgumentResolver implements MethodArgumentResolver {
    /**
     * TODO:改进到也支持List
     *
     * @param parameter
     * @return
     */
    @Override
    public boolean supports(MethodParameter parameter) {
        Class<?> paramType = parameter.getParamType();
        return isFileType(paramType)
                || (paramType.isArray() && isFileType(paramType.getComponentType()))
                || (ReflectionUtils.isAssignable(List.class, paramType) && isFileType(ReflectionUtils.getActualArgument(parameter.getParameter())[0]));
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
            List<?> files = handleMultiFile(request.getParts(), paramType.getComponentType(), paramName);
            Object fileArray = Array.newInstance(paramType.getComponentType(), files.size());
            for (int i = 0; i < files.size(); i++) {
                Array.set(fileArray,i,files.get(i));
            }
            return fileArray;
        } else if (ReflectionUtils.isAssignable(List.class, paramType)) {
            return handleMultiFile(request.getParts(), paramType.getComponentType(),paramName);
        } else {
            return handleSingleFile(request.getPart(paramName), paramType);
        }

    }

    private <T> List<T> handleMultiFile(Collection<Part> parts, Class<T> fileType,String name) {
        List<T> files = new ArrayList<>();
        for (Part part : parts) {
            if (name.equals(part.getName())) {
                T file = handleSingleFile(part, fileType);
                files.add(file);
            }
        }
        return files;
    }

    private <T> T handleSingleFile(Part part, Class<T> paramType) {
        if (Part.class == paramType) {
            return (T) part;
        } else {
            return (T) new StandardMultipartFile(part, getFileName(part));
        }
    }

    private String getFileName(Part part) {
        return part.getSubmittedFileName();
    }

    /**
     * spring mvc中是类似这样写的，但这里简化了，我们框架没有用到这个方法
     *
     * @param disposition
     * @return
     */
    @Deprecated
    private String getFileName(String disposition) {
        String fileName = disposition.substring(disposition.indexOf("filename=\"") + 10, disposition.lastIndexOf("\""));
        System.out.println(fileName);
        return fileName;
    }
}
