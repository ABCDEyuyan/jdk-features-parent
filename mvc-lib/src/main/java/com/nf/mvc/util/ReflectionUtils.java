package com.nf.mvc.util;

import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import nonapi.io.github.classgraph.utils.Assert;

import java.lang.reflect.Method;
import java.net.URI;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 此类的代码参考了spring 的ClassUtils，ReflectionUtils,BeanUtils
 */
public abstract class ReflectionUtils {

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

    public static void main(String[] args) {
        Pattern pattern = Pattern.compile("^set[A-Z].*");
        System.out.println("pattern.matcher(\"set\").matches() = " + pattern.matcher("setAA").matches());
    }

    public static boolean isSimpleProperty(Class<?> type) {
        return isSimpleType(type) || (type.isArray() && isSimpleType(type.getComponentType()));
    }

    public static boolean isSimpleArrayType(Class<?> type) {
        return  type.isArray() && isSimpleType(type.getComponentType());
    }


    public static boolean isCollection(Class<?> type) {
        return Collection.class.isAssignableFrom(type) ||
                Map.class.isAssignableFrom(type);
    }

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

    public static boolean isPrimitiveWrapper(Class<?> clazz) {
        return primitiveWrapperTypeMap.containsKey(clazz);
    }

    public static boolean isPrimitiveOrWrapper(Class<?> clazz) {
        return (clazz.isPrimitive() || isPrimitiveWrapper(clazz));
    }

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

}
