package com.nf.mvc.util;

import com.nf.mvc.support.WebTypeConverter;
import com.nf.mvc.support.converter.*;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.*;

public abstract class WebTypeConverterUtils {
    //设置一个初始的容量，可以避免一些不必要的多次扩容
    private static List<WebTypeConverter> converters = new ArrayList<>(32);

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
     * @param paramType
     * @return 返回null表示没有此类型的类型转换器
     */
    public static WebTypeConverter getTypeConverter(Class<?> paramType) {
        for (WebTypeConverter typeConverter : converters) {
            if (typeConverter.supports(paramType)) {
                return typeConverter;
            }
        }
        return null;
    }


    public static Object toSimpleTypeValue(Class<?> paramType, String requestParamValue) throws Exception {
        WebTypeConverter typeConverter = getTypeConverter(paramType);

        if (typeConverter == null) {
            throw new UnsupportedOperationException("不支持对此类型" + paramType.getName() + "的类型转换");
        }
        return typeConverter.convert(requestParamValue);

    }

    public static Object toSimpleTypeArray(Class<?> componentType, String[] requestParamValues) throws Exception {

        //不要写这样的代码，因为实例化后可能是一个int[]或者double[],是不能转换为Object[]的
        //Object[] values = (Object[]) Array.newInstance(componentType, requestParamValues.length);
        Object array = Array.newInstance(componentType, requestParamValues.length);
        for (int i = 0; i < requestParamValues.length; i++) {
            Array.set(array, i, toSimpleTypeValue(componentType, requestParamValues[i]));
        }
        return array;
    }

    /**
     * 只处理了List与Set集合，其它集合暂不支持
     *
     * @param paramType        参数类型，比如List<String>,List<Integer>这样的类型
     * @param paramGenericType :泛型实参类型，这里只处理简单类型，不处理复杂类型
     * @param requestParamName
     * @param request
     * @return
     * @throws Exception
     */
    public static <T> Collection<T> toCollection(Class<?> paramType, Class<T> paramGenericType,String[] requestParamValues) throws Exception {
        Collection<T> collection = null;

        Object array = toSimpleTypeArray(paramGenericType, requestParamValues);
        if (List.class.isAssignableFrom(paramType)) {
            collection = new ArrayList<>();
        } else if (Set.class.isAssignableFrom(paramType)) {
            collection = new HashSet<>();
        }
        for (int i = 0; i < requestParamValues.length; i++) {

            collection.add((T)Array.get(array,i));
        }
        return collection;
    }

}
