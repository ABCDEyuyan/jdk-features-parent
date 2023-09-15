package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 此抽象解析器对通常的类型进行解析，给那些支持对某个标量类型T，T[],List<T>这样情况的解析器继承用的，比如Integer,Integer[],List<Integer>这样的参数类型或
 * MultipartFile，MultipartFile[],List<MultipartFile>这样的情况。
 * 并不要求所有的参数解析器继承此类型，比如@RequestBody修饰的参数的解析器就完全没有必要继承此类型,servlet Api的参数解析器也不需要继承此类
 *
 * @see RequestBodyMethodArguementResolver
 * @see MultipartFileMethodArgumentResolver
 * @see SimpleTypeMethodArguementResolver
 * @see ServletApiMethodArgumentResolver
 */
public abstract class AbstractCommonTypeMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        return isScalarType(parameter) ||
                isScalarTypeArray(parameter) ||
                isScalarTypeList(parameter);
    }

    // 这里加final修饰方法，是不想让子类重写，因为支持的情况就这三种，逻辑是固定的
    @Override
    public final Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        /*  请求中根本没有对应参数名的请求数据时，source可能就是null的，
          这里不直接依据source为null值提前结束方法的执行，是因为简单类型参数解析器还需要对这些null值进行一些额外的逻辑处理，
          比如获取参数上的注解RequestParam注解指定的默认值。数据源为null的处理逻辑应该由具体的子类去处理，并不应该在父类这里处理
         */
        Object[] source = getSource(parameter, request);
        /* 如果source为null，会返回一个长度为0的空数组，toObjectArray方法并不是一定要调用的，调用可以避免空引用与数组索引越界的异常 */
        Object[] objectArray = ObjectUtils.toObjectArray(source);

     /*   // 关于if else的常见优化方案见https://www.cnblogs.com/jay-huaxiao/p/12586598.html
          // 这里分支不多，保留if else写法显得更清晰，可以不用优化，比较有效的优化方式是利用三目运算符
        if (isScalarType(parameter)) {
            return resolveScalarType(parameter.getParameterType(), Array.getLength(objectArray) == 0 ? null : objectArray[0], parameter);
        } else if (isScalarTypeArray(parameter)) {
            return resolveScalarTypeArray(parameter.getComponentType(), objectArray, parameter);
        } else if (isScalarTypeList(parameter)) {
            return resolveScalarTypeList(parameter.getFirstActualTypeArgument(), objectArray, parameter);
        }
        // 代码基本不会走到这里，因为supports方法只会对通常的类型进行处理，在上面的几个if else分支都分别进行了处理
        return null;*/

        return isScalarType(parameter) ? resolveScalarType(parameter.getParameterType(), Array.getLength(objectArray) == 0 ? null : objectArray[0], parameter)
                : isScalarTypeArray(parameter) ? resolveScalarTypeArray(parameter.getComponentType(), objectArray, parameter)
                : resolveScalarTypeList(parameter.getFirstActualTypeArgument(), objectArray, parameter);
    }

    /**
     * 判断参数解析器是否支持指定的类型
     *
     * @param scalarType 实际要处理的类型，比如方法参数是Integer类型，那么传递给此方法的type就是Integer.class,如果参数是Integer[]，那么type仍然是Integer.class
     *                   如果参数是List<Integer>,那么type仍然是Integer.class
     * @return true表示支持对此类型的解析
     */
    protected abstract boolean supportsInternal(Class<?> scalarType);

    /**
     * 此方法只负责标量（Scalar）类型的值进行解析，不会对标量类型的数组，List进行处理，因为在resolveArgument方法里已经进行了处理
     *
     * @param scalarType      标量类型
     * @param parameterValue  未解析的原始值
     * @param methodParameter 方法参数
     * @return 返回解析后的正确类型的值
     * @throws Exception 抛出解析过程中产生的异常
     */
    protected abstract Object resolveScalarType(Class<?> scalarType, Object parameterValue, MethodParameter methodParameter) throws Exception;

    /**
     * 此方法用来获取请求中的数据源的，数据源主要是request.getParameterValues(name)与request.getParts()两大类<br/>
     * 这里通过这个方法抽象化了2种不同的数据获取方式
     *
     * @param methodParameter：方法参数，利用它可以获取参数名之内的信息
     * @param request：请求对象
     * @return 通常返回某个key下的数组数据，即便只有一个数据，也以数组的形式返回
     */
    protected abstract Object[] getSource(MethodParameter methodParameter, HttpServletRequest request);

    private boolean isScalarType(MethodParameter methodParameter) {
        return supportsInternal(methodParameter.getParameterType());
    }

    private boolean isScalarTypeArray(MethodParameter methodParameter) {
        return methodParameter.isArray() && supportsInternal(methodParameter.getComponentType());
    }

    private boolean isScalarTypeList(MethodParameter methodParameter) {
        return methodParameter.isList() && supportsInternal(methodParameter.getFirstActualTypeArgument());
    }

    private Object resolveScalarTypeArray(Class<?> scalarType, Object[] values, MethodParameter parameter) throws Exception {
        /* 如果数据源为null，那么length就=0，那么array就是一个长度为0的数组，并不会进入到循环里面执行真正的参数处理 */
        int length = Array.getLength(values);
        Object array = Array.newInstance(scalarType, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, resolveScalarType(parameter.getComponentType(), Array.get(values, i), parameter));
        }
        return array;
    }

    private List resolveScalarTypeList(Class<?> scalarType, Object[] values, MethodParameter parameter) throws Exception {
        List list = new ArrayList<>();
        for (int i = 0; i < Array.getLength(values); i++) {
            list.add(resolveScalarType(scalarType, Array.get(values, i), parameter));
        }
        return list;
    }
}
