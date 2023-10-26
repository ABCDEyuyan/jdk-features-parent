package com.nf.mvc.util;

import com.nf.mvc.MvcContext;
import com.nf.mvc.argument.BeanMethodArgumentResolver;
import com.nf.mvc.argument.MethodParameter;
import com.nf.mvc.handler.HandlerClass;
import com.nf.mvc.ioc.Injected;
import javassist.Modifier;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

import javax.servlet.http.HttpServletRequest;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.*;

/**
 * 此类的代码参考了spring 的ClassUtils，ReflectionUtils,BeanUtils
 */
public abstract class ReflectionUtils {

    /**
     * 这个map集合，key是包装类型（wrapper），值是此包装类型对应的基本类型(primitive)
     * IdentityHashMap表示的是key用==符号比较是true才相等，而不是普通HashMap用equals比较
     * ==符号表示指向的是同一个对象，equals表示的内容相等，简单来说，==是true就表示两者完全一样，
     * 那么equals也肯定是true，但equals是true不一定==也是true
     */
    private static final Map<Class<?>, Class<?>> WRAPPER_TYPE_TO_PRIMITIVE_MAP = new IdentityHashMap<>(9);
    private static final Map<Class<?>, Class<?>> PRIMITIVE_TYPE_TO_WRAPPER_MAP = new IdentityHashMap<>(9);

    private static final String GETTER_METHOD_Pattern = "^get[A-Z].*";
    private static final String GETTER_IS_METHOD_Pattern = "^is[A-Z].*";
    private static final String SETTER_METHOD_Pattern = "^set[A-Z].*";
    static {
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Boolean.class, boolean.class);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Byte.class, byte.class);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Character.class, char.class);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Double.class, double.class);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Float.class, float.class);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Integer.class, int.class);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Long.class, long.class);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Short.class, short.class);
        WRAPPER_TYPE_TO_PRIMITIVE_MAP.put(Void.class, void.class);

        for (Map.Entry<Class<?>, Class<?>> entry : WRAPPER_TYPE_TO_PRIMITIVE_MAP.entrySet()) {
            PRIMITIVE_TYPE_TO_WRAPPER_MAP.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * 现在这种写法是默认调用class的默认构造函数来实例化对象的
     * 暂时没有考虑调用其它构造函数的情况
     * <h3>使用地方</h3>
     * <p>整个mvc框架都用的这个方法来创建被mvc管理的类的对象，主要使用的地方有以下几个
     * <ul>
     *     <li>实例化扫描到的Mvc核心类，详见{@link com.nf.mvc.MvcContext#resolveMvcClass(Class, Class, List)},这些类型是单例的</li>
     *     <li>实例化控制器bean类型的方法参数，详见{@link BeanMethodArgumentResolver#resolveSetterArgument(MethodParameter, HttpServletRequest, Stack)},这些实例是原型的</li>
     *     <li>实例化用户编写的后端控制器，详见{@link HandlerClass#getHandlerObject()},这些实例是原型的</li>
     * </ul>
     * </p>
     * @param clz 用来实例化的class
     * @return 此class的实例
     */
    public static <T> T newInstance(Class<? extends T> clz) {
        T instance ;
        try {
            instance =  clz.newInstance();
            injectConfigurationProperties(instance);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException("无法实例化对象，类:" + clz.getName() + " 是否没有提供默认构造函数?", e);
        }
        return instance;
    }

    /**
     * 此方法目前只是用来注入配置属性类使用的
     * <p>此方法本不应该写在这里,因为它与注入有关,但不想增加复杂性,
     * 也不想给mvc框架提供ioc的能力,就简单注入配置属性类,所以就写在了这里</p>
     *
     * <p>此方法也应该设计为抛出Exception更好，这里抛出的调用setAccessible方法时产生的异常，
     * 主要是为了在{@link #newInstance(Class)}方法里演示catch的或（|）写法</p>
     * @param instance 某个需要注入配置属性的实例
     * @param <T> 实例的类型
     * @throws IllegalAccessException 注入ConfigurationProperties时产生的异常
     */
    private static <T> void injectConfigurationProperties(T instance) throws IllegalAccessException {
        Field[] fields = instance.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Injected.class)) {
                field.setAccessible(true);
                Object configProperties = MvcContext.getMvcContext().getConfigurationProperties().get(field.getType());
                field.set(instance,configProperties );
                field.setAccessible(false);
            }
        }
    }

    public static List<Method> getAllSetterMethods(Class<?> clz) {
        List<Method> setterMethods = new ArrayList<>();
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            if (ReflectionUtils.isSetter(method)) {
                setterMethods.add(method);
            }
        }
        return setterMethods;
    }

    /**
     * 参考
     * https://blog.csdn.net/hehuanchun0311/article/details/79755266
     * 与https://blog.csdn.net/wwzmvp/article/details/116302782
     * http://lzxz1234.github.io/java/2014/07/25/Get-Method-Parameter-Names-With-Javassist.html
     *
     * @param clazz:方法所在的类
     * @param methodName：方法的名字
     * @param paramTypes：方法的参数类型，以便支持重载
     * @return 方法各个参数的名字（依据参数位置顺序依次返回）
     */
    @Deprecated
    public static List<String> getParamNamesWithParamType(Class<?> clazz, String methodName, Class<?>... paramTypes) {
        List<String> paramNames = new ArrayList<>();
        ClassPool pool = ClassPool.getDefault();
        int paramLength = 0;
        /*不加下面的insertClassPath这行代码会出现ClassNotFound异常，解决办法如下
        含义是告诉javassist也去RefelctionUtils类所在的类路径下去查找类*/
        pool.insertClassPath(new ClassClassPath(ReflectionUtils.class));
        try {
            CtClass ctClass = pool.getCtClass(clazz.getName());
            CtMethod ctMethod;
            if (paramTypes != null && paramTypes.length > 0) {
                CtClass[] paramClasses = new CtClass[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    paramClasses[i] = pool.get(paramTypes[i].getName());
                }
                ctMethod = ctClass.getDeclaredMethod(methodName, paramClasses);
            } else {
                ctMethod = ctClass.getDeclaredMethod(methodName);
                paramLength = ctMethod.getParameterTypes().length;
            }
            // 使用javassist的反射方法的参数名
            javassist.bytecode.MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                //必须是对索引进行排序，所以选定一个可以排序的集合
                TreeMap<Integer, String> sortMap = new TreeMap<>();
                for (int i = 0; i < attr.tableLength(); i++) {
                    sortMap.put(attr.index(i), attr.variableName(i));
                }
                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                paramNames = Arrays.asList(Arrays.copyOfRange(sortMap.values().toArray(new String[0]), pos, paramLength + pos));
                return paramNames;
            }
            return paramNames;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Deprecated
    public static List<String> getParamNames(Class<?> clazz, String methodName) {
        return getParamNamesWithParamType(clazz, methodName);
    }

    public static List<String> getParameterNames(Method method) {
        Parameter[] parameters = method.getParameters();
        List<String> parameterNames = new ArrayList<>();

        for (Parameter parameter : parameters) {
            if (!parameter.isNamePresent()) {
                throw new IllegalStateException("编译的时候需要指定-parameters选项");
            }
            String parameterName = parameter.getName();
            parameterNames.add(parameterName);
        }
        return parameterNames;
    }

    /**
     * <a href="https://www.runoob.com/regexp/regexp-syntax.html">正则表达式入门教程</a>
     * <ul>
     * <li>^:表示以什么开始，^set意思就是以set开头</li>
     * <li>[A-Z] ： 表示一个区间，匹配所有大写字母，[a-z] 表示所有小写字母</li>
     * <li>句号：匹配除换行符（\n、\r）之外的任何单个字符</li>
     * <li>‘*’：匹配前面的子表达式零次或多次,在下面的例子就是前面的句号</li>
     * </ul>
     * <p>所以^set[A-Z].* 意思就是以set开头，之后跟一个大写字母，大写字母之后可以出现0个或多个字符</p>
     *
     * @param method 方法
     * @return 是setter方法就返回true，否则返回false
     */
    public static boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches(SETTER_METHOD_Pattern);
    }

    public static boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0) {
            if (method.getName().matches(GETTER_METHOD_Pattern) &&
                    !method.getReturnType().equals(void.class)) {
                return true;
            }
            return method.getName().matches(GETTER_IS_METHOD_Pattern) &&
                    method.getReturnType().equals(boolean.class);
        }
        return false;
    }

    /**
     * 是简单类型或者简单类型的数组就认为是一个简单属性
     * 比如int，Integer这种就是简单类型（SimpleType）
     * 是一个数组，并且数组的成员（Component）是简单类型，就认为是一个简单属性
     * 比如int[],Integer[],String[]就认为是简单属性，但Emp[]不是简单属性，因为其成员Emp不是简单类型
     *
     * @param type 类型信息
     * @return 基本类型及其包装类型或是这些类型的数组类型就返回true
     */
    public static boolean isSimpleProperty(Class<?> type) {
        return isSimpleType(type) || (type.isArray() && isSimpleType(type.getComponentType()));
    }

    /**
     * 通常指的就是我们自己写的pojo类，比如Emp
     * 判断是<b><i>不严谨</i></b>的，比如我们自己写的一个Servlet的类，它也是返回true
     * <p>
     * 这个方法的初衷是想让我们自己写的pojo类才返回true
     * 我们的pojo类通常不继承任何父类，就是普通的属性的封装
     *
     * @param type 类型信息
     * @return 非简单类型并且也不是集合类型就返回true
     */
    public static boolean isComplexProperty(Class<?> type) {
        return !isSimpleProperty(type) && !isCollection(type);
    }

    /**
     * 判断是否是简单类型的数组，比如int[],Integer[],Date[]
     * @param type 数据类型
     * @return 简单类型就返回true，否则返回false
     */
    public static boolean isSimpleTypeArray(Class<?> type) {
        return type.isArray() && isSimpleType(type.getComponentType());
    }

    public static boolean isSimpleCollection(Class<?> type) {
        return isAssignable(List.class, type) ||
                isAssignable(Set.class, type) ||
                isAssignable(Map.class, type);
    }

    public static boolean isList(Class<?> type) {
        return isAssignable(List.class, type);
    }

    public static boolean isSet(Class<?> type) {
        return isAssignable(Set.class, type);
    }

    public static boolean isSimpleTypeCollection(Class<?> collectionType, Class<?> actualParamType) {
        return isSimpleCollection(collectionType) && isSimpleType(actualParamType);
    }

    public static boolean isSimpleTypeList(Class<?> listType, Class<?> actualTypeParam) {
        return isList(listType) && isSimpleType(actualTypeParam);
    }

    /**
     * 如果是Collection以及Map的子类型，就认为是一个集合
     *
     * @param type 类型信息
     * @return 是Collection或者是Map就返回true，否则返回false
     */
    public static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type) ||
                Map.class.isAssignableFrom(type);
    }


    /**
     * 如果是基本类型、包装类型或者date，number等等就认为是简单类型
     *
     * @param type 类型信息
     * @return 基本类型、包装类型或者date，number类型就返回true，否则返回false
     */
    public static boolean isSimpleType(Class<?> type) {
        return (Void.class != type && void.class != type &&
                (isPrimitiveOrWrapper(type) ||
                        Enum.class.isAssignableFrom(type) ||
                        CharSequence.class.isAssignableFrom(type) ||
                        Number.class.isAssignableFrom(type) ||
                        Date.class.isAssignableFrom(type) ||
                        Temporal.class.isAssignableFrom(type) ||
                        URI.class == type ||
                        URL.class == type ||
                        Locale.class == type ||
                        Class.class == type) ||
                LocalDate.class == type ||
                LocalDateTime.class == type
        );
    }

    /**
     * 判断是否是基本类型的包装类型，比如传递的参数是Integer就会返回true
     *
     * @param clazz：包装类型
     * @return 是包装类型就返回true，否则返回false
     */
    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return WRAPPER_TYPE_TO_PRIMITIVE_MAP.containsKey(clazz);
    }


    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive();
    }

    /**
     * 判断一个类是否是简单类型或者简单类型对应的包装类型
     *
     * @param clazz 类型信息
     * @return 是基本类型或包装类型就返回true，否则返回false
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * 判断第二个参数是否是第一个参数的子类或者实现类
     * 也考虑了基本类型，比如Integer与int认为是isAssignable
     * assignable：可赋值的
     *
     * @param lhsType 左手边类型信息
     * @param rhsType 右手边类型信息
     * @return 右手边类型可以赋值给左手边类型时返回true，否则返回false
     */
    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }
        if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = WRAPPER_TYPE_TO_PRIMITIVE_MAP.get(rhsType);
            return (lhsType == resolvedPrimitive);
        } else {
            Class<?> resolvedWrapper = PRIMITIVE_TYPE_TO_WRAPPER_MAP.get(rhsType);
            return (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper));
        }
    }

    public static boolean isAssignableToAny(Class<?> lhsType, Class<?>... rhsTypes) {
        boolean isAssignable = false;
        for (Class<?> rhsType : rhsTypes) {
            isAssignable = isAssignable(rhsType, lhsType);
            if (isAssignable) {
                break;
            }
        }
        return isAssignable;
    }

    /**
     * 此方法是用来获取方法泛型参数的类型实参的
     *
     * @param parameter 方法参数信息,要求是一个泛型实参类型
     * @return 返回所有的泛型实参类型信息
     */
    public static Class<?>[] getActualArgument(Parameter parameter) {

        Type type = parameter.getParameterizedType();
        if (!(type instanceof ParameterizedType)) {
            throw new IllegalArgumentException("参数要求是一个参数化的泛型类型，不能使用原生类型");
        }

        // 如果方法的参数是List这样的类型，而不是List<String>,List<Integer>这样的，直接进行类型转换抛出ClassCastException异常
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        Class<?>[] actualTypeArguments = new Class[types.length];
        for (int i = 0; i < types.length; i++) {
            actualTypeArguments[i] = (Class<?>) types[i];
        }
        return actualTypeArguments;
    }

    public static void setFieldValue(Object instance, Field field, Object value)  {
        try {
            field.setAccessible(true);
            field.set(instance, value);
            field.setAccessible(false);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("字段值设置失败",e);
        }
    }

    //=======================注解相关=============================
    public static <T> T getAnnoValue( AnnotatedElement ele,Class<? extends Annotation> annoClass){
        return getAnnoValue(ele, annoClass, "value");
    }

    @SuppressWarnings("unchecked")
    public static <T> T getAnnoValue(AnnotatedElement ele,Class<? extends Annotation> annoClass,String attrName){
        if (!ele.isAnnotationPresent(annoClass)) {
            throw new IllegalStateException("元素:" + ele + " 上没有注解:" + annoClass.getName());
        }
        Annotation anno = ele.getDeclaredAnnotation(annoClass);
        T result;
        try {
            Method valueMethod = annoClass.getDeclaredMethod(attrName);
            result = (T)valueMethod.invoke(anno);
        } catch (Exception e) {
            throw new IllegalStateException("注解:" + annoClass.getName() + " 没有value属性");
        }
        return result;
    }
}
