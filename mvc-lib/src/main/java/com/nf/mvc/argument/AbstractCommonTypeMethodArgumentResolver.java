package com.nf.mvc.argument;

import com.nf.mvc.MethodArgumentResolver;
import com.nf.mvc.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * 此抽象解析器对通常的类型进行解析，给那些支持对某个标量类型T，T[],List<T>,Set<T>等等这样情况的解析器继承用的，
 * 比如Integer,Integer[],List<Integer>这样的参数类型或MultipartFile，MultipartFile[],List<MultipartFile>这样的情况。
 * 并不要求所有的参数解析器继承此类型，比如@RequestBody修饰的参数解析器就完全没有必要继承此类型,ServletApi的参数解析器也不需要继承此类
 * <p>我内心还有另外一种思路,当前没有实现,现在记录在这个注释里.就是把这个类改名为ContainerTypeMethodArgumentResolver,
 * 代表某个标量类型的容器类型,比如Integer的数组,Integer的List集合等,并把此解析器作为整个解析器链的最后一个解析器,
 * 这样所有其他解析器支持的类型就不需要处理容器类型的解析,比如List<Integer>,统统就交给本类处理了.
 * 本类的职责就是负责实例化容器类,比如List,数组,Set等,并确定如何从请求端获取数据,比如规定请求数据格式为
 * employees[0].id=100,employees[0].name="cj",employees[1].id=200,employees[1].name="cj2",那么就创建一个List<Employee>
 * 的对象,其中有两条记录,而解析employee类型就交给其它的解析器去完成.这个解析器实现时有个重点注意点,
 * 就是与BeanMethodArgumentResolver解析器谁在解析器链的最后面问题,因为这两个解析器都需要用到解析器链的
 * 其他所有解析器来解析参数/p>
 *
 * @author cj
 * @see RequestBodyMethodArgumentResolver
 * @see MultipartFileMethodArgumentResolver
 * @see SimpleTypeMethodArgumentResolver
 * @see ServletApiMethodArgumentResolver
 */
public abstract class AbstractCommonTypeMethodArgumentResolver implements MethodArgumentResolver {
    @Override
    public boolean supports(MethodParameter parameter) {
        return isScalarType(parameter) ||
                isScalarTypeArray(parameter) ||
                isScalarTypeList(parameter);
    }

    /**
     * 这里用final修饰方法，是不想让子类重写，因为支持的情况目前就这么三种，逻辑是固定的,
     * 如果以后要支持其他类型,比如Set<T>,Map<String,T>这样的情况,只需要在此父类添加相关实现即可,
     * 子类是不需要额外再改动的
     *
     * @param parameter 方法参数
     * @param request   servlet请求对象
     * @return 返回解析之后的参数结果值
     * @throws Exception 解析过程中可能抛出的异常
     */
    @Override
    public final Object resolveArgument(MethodParameter parameter, HttpServletRequest request) throws Exception {
        /*  请求中根本没有对应参数名的请求数据时，source可能就是null的，
          这里不直接依据source为null值提前结束方法的执行，是因为简单类型参数解析器还需要对这些null值进行一些额外的逻辑处理，
          比如获取参数上的注解RequestParam注解指定的默认值。数据源为null的处理逻辑应该由具体的子类去处理，并不应该在父类这里处理
         */
        Object[] source = getSource(parameter, request);
        /* 如果source为null，会返回一个长度为0的空数组，toObjectArray方法并不是一定要调用的，调用可以避免空引用与数组索引越界的异常 */
        Object[] objectArray = ObjectUtils.toObjectArray(source);
        // 用三目运算符取代if else
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
     * 此方法用来获取请求中的数据源的，数据源主要是request.getParameterValues(name)与request.getParts()两大类<br/>
     * 这里通过这个方法抽象化了2种不同的数据获取方式
     *
     * @param methodParameter：方法参数，利用它可以获取参数名之内的信息
     * @param request：请求对象
     * @return 通常返回某个key下的数组数据，即便只有一个数据，也以数组的形式返回
     */
    protected abstract Object[] getSource(MethodParameter methodParameter, HttpServletRequest request);

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
     * 这里如果把方法签名改为<T> T[] resolveScalarTypeArray(Class<T>,,,)这种形式，那么泛型擦除会导致方法签名是返回Object[],
     * 那么在处理int[]这种简单类型数组作为控制器参数时，会导致报无法把简单类型数组转换为Object[]数组的异常的问题，所以没有把本类或这3个解析方法设计为泛型
     *
     * @param scalarType 标量（单个）类型，数组的成员类型或者泛型集合实参类型，比如List<String>的String
     * @param values     参数值
     * @param parameter  控制器方法参数
     * @return 返回解析后的单个参数的值
     * @throws Exception 解析过程中抛出的异常
     */
    private Object resolveScalarTypeArray(Class<?> scalarType, Object[] values, MethodParameter parameter) throws Exception {
        /* 如果数据源为null，那么length就=0，那么array就是一个长度为0的数组，并不会进入到循环里面执行真正的参数处理
        //不要写这样的代码，因为实例化后可能是一个int[]或者double[],是不能转换为Object[]的
        //Object[] values = (Object[]) Array.newInstance(scalarType, length); */

        int length = Array.getLength(values);
        Object array = Array.newInstance(scalarType, length);
        for (int i = 0; i < length; i++) {
            Array.set(array, i, resolveScalarType(parameter.getComponentType(), Array.get(values, i), parameter));
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> resolveScalarTypeList(Class<T> scalarType, Object[] values, MethodParameter parameter) throws Exception {
        List<T> list = new ArrayList<>();
        for (int i = 0; i < Array.getLength(values); i++) {
            list.add((T) resolveScalarType(scalarType, Array.get(values, i), parameter));
        }
        return list;
    }


    private boolean isScalarType(MethodParameter methodParameter) {
        return supportsInternal(methodParameter.getParameterType());
    }

    private boolean isScalarTypeArray(MethodParameter methodParameter) {
        return methodParameter.isArray() && supportsInternal(methodParameter.getComponentType());
    }

    private boolean isScalarTypeList(MethodParameter methodParameter) {
        return methodParameter.isList() && supportsInternal(methodParameter.getFirstActualTypeArgument());
    }
}
