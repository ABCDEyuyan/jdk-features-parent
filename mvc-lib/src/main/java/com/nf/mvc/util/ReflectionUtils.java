package com.nf.mvc.util;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;

import java.lang.reflect.Method;
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

    /**这个map集合，key是包装类型（wrapper），值是此包装类型对应的基本类型(primitive)
     * IdentityHashMap表示的是key用==符号比较是true才相等，而不是普通HashMap用equals比较
     * ==符号表示指向的是同一个对象，equals表示的内容相等，简单来说，==是true就表示两者完全一样，
     * 那么equals也肯定是true，但equals是true不一定==也是true
     * */
    private static final Map<Class<?>, Class<?>> primitiveWrapperTypeMap = new IdentityHashMap<>(9);
    private static final Map<Class<?>, Class<?>> primitiveTypeToWrapperMap = new IdentityHashMap<>(9);

    static {
        primitiveWrapperTypeMap.put(Boolean.class, boolean.class);
        primitiveWrapperTypeMap.put(Byte.class, byte.class);
        primitiveWrapperTypeMap.put(Character.class, char.class);
        primitiveWrapperTypeMap.put(Double.class, double.class);
        primitiveWrapperTypeMap.put(Float.class, float.class);
        primitiveWrapperTypeMap.put(Integer.class, int.class);
        primitiveWrapperTypeMap.put(Long.class, long.class);
        primitiveWrapperTypeMap.put(Short.class, short.class);
        primitiveWrapperTypeMap.put(Void.class, void.class);

        for (Map.Entry<Class<?>, Class<?>> entry : primitiveWrapperTypeMap.entrySet()) {
            primitiveTypeToWrapperMap.put(entry.getValue(), entry.getKey());
        }
    }

    /**
     * 现在这种写法是默认调用class的默认构造函数来实例化对象的
     * 暂时没有考虑调用其它构造函数的情况
     * @param clz
     * @return
     */
    public static <T> T newInstance(Class<? extends T> clz){
        try {
            return clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
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
     *
     * @param clazz:方法所在的类
     * @param methodName：方法的名字
     * @param paramTypes：方法的参数类型，以便支持重载
     * @return 方法各个参数的名字（依据参数位置顺序依次返回）
     */
    public static List<String> getParamNamesWithParamType(Class<?> clazz, String methodName, Class... paramTypes) {
        List<String> paramNames = new ArrayList<>();
        ClassPool pool = ClassPool.getDefault();
        //不加下面的insertClassPath这行代码会出现ClassNotFound异常，解决办法
        // 参考https://blog.csdn.net/hehuanchun0311/article/details/79755266与https://blog.csdn.net/wwzmvp/article/details/116302782
        //但第二篇文章中说的更换javassist版本经测试并没有解决问题，所以采用下面的方案

        //含义是告诉javassist也去RefelctionUtils类所在的类路径下去查找类
        pool.insertClassPath(new ClassClassPath(ReflectionUtils.class));
        try {
            CtClass ctClass = pool.getCtClass(clazz.getName());
            CtMethod ctMethod = null;
            if(paramTypes!=null && paramTypes.length>0) {
                CtClass[] paramClasses = new CtClass[paramTypes.length];
                for (int i = 0; i < paramTypes.length; i++) {
                    paramClasses[i] = pool.get(paramTypes[i].getName());
                }
                 ctMethod = ctClass.getDeclaredMethod(methodName, paramClasses);
            }else{
                ctMethod = ctClass.getDeclaredMethod(methodName);
            }
            // 使用javassist的反射方法的参数名
            javassist.bytecode.MethodInfo methodInfo = ctMethod.getMethodInfo();
            CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
            LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
            if (attr != null) {
                int len = ctMethod.getParameterTypes().length;
                // 非静态的成员函数的第一个参数是this
                int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
                for (int i = 0; i < len; i++) {
                    paramNames.add(attr.variableName(i + pos));
                }

            }
            return paramNames;
        } catch (NotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static List<String> getParamNames(Class<?> clazz, String methodName) {
        return getParamNamesWithParamType(clazz, methodName);
    }

    /**
     *
     * <a href="https://www.runoob.com/regexp/regexp-syntax.html">正则表达式入门教程</a>
     * <ul>
     * <li>^:表示以什么开始，^set意思就是以set开头</li>
     * <li>[A-Z] ： 表示一个区间，匹配所有大写字母，[a-z] 表示所有小写字母</li>
     * <li>句号：匹配除换行符（\n、\r）之外的任何单个字符</li>
     * <li>‘*’：匹配前面的子表达式零次或多次,在下面的例子就是前面的句号</li>
     * </ul>
     * <p>所以^set[A-Z].* 意思就是以set开头，之后跟一个大写字母，大写字母之后可以出现0个或多个字符</p>
     * @param method
     * @return
     */
    public static boolean isSetter(Method method) {
        return Modifier.isPublic(method.getModifiers()) &&
                method.getReturnType().equals(void.class) &&
                method.getParameterTypes().length == 1 &&
                method.getName().matches("^set[A-Z].*");
    }

    public static boolean isGetter(Method method) {
        if (Modifier.isPublic(method.getModifiers()) &&
                method.getParameterTypes().length == 0) {
            if (method.getName().matches("^get[A-Z].*") &&
                    !method.getReturnType().equals(void.class))
                return true;
            if (method.getName().matches("^is[A-Z].*") &&
                    method.getReturnType().equals(boolean.class))
                return true;
        }
        return false;
    }

    /**
     * 是简单类型或者简单类型的数组就认为是一个简单属性
     * 比如int，Integer这种就是简单类型（SimpleType）
     * 是一个数组，并且数组的成员（Component）是简单类型，就认为是一个简单属性
     * 比如int[],Integer[],String[]就认为是简单属性，但Emp[]不是简单属性，因为其成员Emp不是简单类型
     * @param type
     * @return
     */
    public static boolean isSimpleProperty(Class<?> type) {
        return isSimpleType(type) || (type.isArray() && isSimpleType(type.getComponentType()));
    }

    /**
     * 通常指的就是我们自己写的pojo类，比如Emp
     * 判断是”不严谨“的，比如我们自己写的一个Servlet的类，它也是返回true
     *
     * 这个方法的初衷是想让我们自己写的pojo类才返回true
     * 我们的pojo类通常不继承任何父类，就是普通的属性的封装
     * @param type
     * @return
     */
    public static boolean isComplexProperty(Class<?> type) {
        return isSimpleProperty(type)==false && isCollection(type)==false;
    }

    /**
     * 判断是否是简单类型的数组，比如int[],Integer[],Date[]
     * @param type
     * @return
     */
    public static boolean isSimpleTypeArray(Class<?> type) {
        return  type.isArray() && isSimpleType(type.getComponentType());
    }


    public static boolean isSimpleCollection(Class<?> type) {
        return isAssignable(List.class,type) ||
                isAssignable(Set.class,type) ||
                isAssignable(Map.class,type);
    }

    public static boolean isSimpleTypeCollection(Class<?> collectionType, Class<?> actualTypeParam) {
        return isSimpleCollection(collectionType) && isSimpleType(actualTypeParam);
    }
    /**
     * 如果是Collection以及Map的子类型，就认为是一个集合
     * @param type
     * @return
     */
    public static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type) ||
                Map.class.isAssignableFrom(type);
    }

    public static boolean isListOrSet(Class<?> type) {
        return List.class.isAssignableFrom(type) ||
                Set.class.isAssignableFrom(type);
    }

    /**
     * 如果是基本类型、包装类型或者date，number等等就认为是简单类型
     * @param type
     * @return
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
     * @param clazz：包装类型
     * @return
     */
    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return primitiveWrapperTypeMap.containsKey(clazz);
    }


    public static boolean isPrimitive(Class<?> clazz) {
        return clazz.isPrimitive();
    }

    /**
     * 判断一个类是否是简单类型或者简单类型对应的包装类型
     * @param clazz
     * @return
     */
    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

    /**
     * 判断第二个参数是否是第一个参数的子类或者实现类
     * 也考虑了基本类型，比如Integer与int认为是isAssignable
     * assignable：可赋值的
     * @param lhsType
     * @param rhsType
     * @return
     */
    public static boolean isAssignable(Class<?> lhsType, Class<?> rhsType) {
        if (lhsType.isAssignableFrom(rhsType)) {
            return true;
        }
        if (lhsType.isPrimitive()) {
            Class<?> resolvedPrimitive = primitiveWrapperTypeMap.get(rhsType);
            return (lhsType == resolvedPrimitive);
        }
        else {
            Class<?> resolvedWrapper = primitiveTypeToWrapperMap.get(rhsType);
            return (resolvedWrapper != null && lhsType.isAssignableFrom(resolvedWrapper));
        }
    }
    public static boolean isAssignableToAny(Class<?> lhsType, Class<?>... rhsTypes) {
       boolean isAssignable = false;
        for (Class<?> rhsType : rhsTypes) {
            isAssignable =  isAssignable(rhsType,lhsType);
            if (isAssignable == true) {
                break;
            }
        }
        return isAssignable;
    }
}
