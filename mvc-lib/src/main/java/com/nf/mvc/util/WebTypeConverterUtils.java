package com.nf.mvc.util;

import com.nf.mvc.support.WebTypeConverter;
import com.nf.mvc.support.converter.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.*;

public abstract class WebTypeConverterUtils {
    //设置一个初始的容量，可以避免一些不必要的多次扩容
    private static List<WebTypeConverter> converters = new ArrayList<>(16);
    static {
        converters.add(new BigDecimalTypeConverter());
        converters.add(new BooleanTypeConverter());
        converters.add(new ByteTypeConverter());
        converters.add(new CharacterTypeConverter());
        converters.add(new DateTypeConverter());
        converters.add(new DoubleTypeConverter());
        converters.add(new FloatTypeConverter());
        converters.add(new IntegerTypeConverter());
        converters.add(new LocalDateTimeTypeConverter());
        converters.add(new LocalDateTypeConverter());
        converters.add(new LongTypeConverter());
        converters.add(new LongTypeConverter());
        converters.add(new ShortTypeConverter());
        converters.add(new StringTypeConverter());
    }

    /**
     *
     * @param paramType
     * @return 返回null表示没有此类型的类型转换器
     */
    public static WebTypeConverter<?> getTypeConverter(Class<?> paramType) {
        for(WebTypeConverter typeConverter : converters) {
            if(typeConverter.supports(paramType)) {
                return typeConverter;
            }
        }
        return null;
    }

    /**
     * 将请求参数转换为具体的值
     * @param paramType
     * @param requestParamName
     * @param request
     * @return
     */
    public static Object toValue(Class<?> paramType, String requestParamName,
                                  HttpServletRequest request) throws Exception{
        Object value = null;
        WebTypeConverter<?> typeConverter = getTypeConverter(paramType);
        if(typeConverter != null) {
            String requestParam = request.getParameter(requestParamName);
            value = typeConverter.convert(requestParam);
        }
        return value;
    }

    public static Object toArray(Class<?> componentType, String requestParamName,
                                    HttpServletRequest request) throws Exception{
        WebTypeConverter<?> typeConverter = getTypeConverter(componentType);
        if(typeConverter != null) {
            String[] requestParams = request.getParameterValues(requestParamName);
            //不要写这样的代码，因为实例化后可能是一个int[]或者double[],是不能转换为Object[]的
            //Object[] values = (Object[]) Array.newInstance(componentType, requestParams.length);
            Object array = Array.newInstance(componentType, requestParams.length);
            for(int i=0; i<requestParams.length; i++) {
                Array.set(array,i,typeConverter.convert(requestParams[i]));
            }
            return array;
        }
        return null;
    }

    /**
     * 只处理了List与Set集合，其它集合暂不支持
     * @param paramType
     * @param paramGenericType
     * @param requestParamName
     * @param request
     * @return
     * @throws Exception
     */
    public static Collection<?> toCollection(Class<?> paramType, Class<?> paramGenericType,
                                              String requestParamName, HttpServletRequest request) throws Exception{
        Collection<?> collection = null;
        Object values = toArray(paramGenericType, requestParamName, request);
        if(List.class.isAssignableFrom(paramType)) {
            collection = Arrays.asList(values);
        } else if(Set.class.isAssignableFrom(paramType)) {
            collection = new HashSet<>(Arrays.asList(values));
        }
        return collection;
    }

}
