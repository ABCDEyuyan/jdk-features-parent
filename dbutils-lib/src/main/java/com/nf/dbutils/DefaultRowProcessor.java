package com.nf.dbutils;

import com.nf.dbutils.util.RsMetaUtils;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ServiceLoader;

public class DefaultRowProcessor implements RowProcessor{

    public static final int   PROPERTY_NOT_FOUND = -1;

    //利用spi技术来找到所有的PropertyHandler接口的实现类
    private static final ServiceLoader<PropertyHandler> propertyHandlers = ServiceLoader.load(PropertyHandler.class);
    private final Map<String,String> propertyOverrides ;

    public DefaultRowProcessor() {
        this(new HashMap<>());
    }

    public DefaultRowProcessor(Map<String, String> propertyOverrides) {

        super();
        if (propertyOverrides == null) {
            throw new DaoException("不能传一个空的映射集合过来");
        }

        this.propertyOverrides = propertyOverrides;
    }

    public Object[] toArray(ResultSet rs) throws SQLException {
        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();
        Object[] row = new Object[cols];

        for (int i = 0; i < cols; i++) {
            //取resultset的数据时，序号是从1开始的，
            // 平时我们都是写sql语句中的字段名(select id,name from emp)
            row[i] = rs.getObject(i + 1);
        }
        return row;
    }

    public Map<String, Object> toMap(ResultSet rs) throws SQLException {
        Map<String, Object> map = new HashMap<>();

        ResultSetMetaData metaData = rs.getMetaData();
        int cols = metaData.getColumnCount();

        for (int i = 1; i <= cols; i++) {
            //取列名的逻辑优先是取别名

            String columnName = RsMetaUtils.getColumnName(metaData,i);
            map.put(columnName, rs.getObject(i));
        }

        return map;

    }

    public <T> T toBean(ResultSet rs,Class<?> clz) throws SQLException {
        T bean = createInstance(clz);

        return  this.populateBean(rs, bean);

    }

    /**
     * 此方法完成bean的属性的赋值
     * @param rs
     * @param bean
     * @param <T>
     * @return
     * @throws SQLException
     */
    public <T> T populateBean(ResultSet rs, T bean) throws SQLException {
       //第1步：获取实体类有多少的属性
        PropertyDescriptor[] props = this.propertyDescriptors(bean.getClass());
        //第二步：下面2行代码，就是解决结果集的列与属性匹配映射的功能
        ResultSetMetaData rsmd = rs.getMetaData();
        int[] columnToProperty = this.mapColumnsToProperties(rsmd, props);

        //第三步：真正给bean的属性赋值的功能
        return populateBean(rs, bean, props, columnToProperty);
    }

    /**
     * 此方法会从数据库取数据，并且处理基本类型的问题
     * 要点：从数据库取值时是null值，并且字段是基本类型，那么就不能赋值为null
     * @param rs
     * @param bean
     * @param props
     * @param columnToProperty
     * @param <T>
     * @return
     * @throws SQLException
     */
    protected <T> T populateBean(ResultSet rs, T bean, PropertyDescriptor[] props, int[] columnToProperty) throws SQLException{

        for (int i = 0; i < columnToProperty.length; i++) {
            //没有映射关系，什么都不处理，直接下一次循环
            if (columnToProperty[i] == PROPERTY_NOT_FOUND) {
                continue;
            }
            //有映射关系，需要处理
            PropertyDescriptor prop = props[columnToProperty[i]];

            Class<?> propertyType = prop.getPropertyType();
            //属性基本不可能为null，所以出现这种情况，什么都不处理
            if (propertyType == null) {
                continue;
            }
            //下面的代码意思就是propertyType不为null并且有映射关系

            //直接利用getObject从数据库取数据
            //不采用原始代码中的processColumn体系，
            // 因为jdbc的Mysql的当前使用的驱动版本，getObject也会去调用getXxx方法
            Object value = rs.getObject(i);

            //不需要像源码那样赋值为0或者false，因为这些属性不处理，它也是这样的值
            //不管属性是基本类型还是包装类型，还是其它类型，数据库取出来的值是null，此属性就不需要特别处理，直接跳过
            if (value == null ) {
                continue;
            }

            //value==null 并且属性不是基本类型或者value不为null
            //value 不为null，属性可能是基本类型也可能不是基本类型

            this.callSetter(bean, prop, value);


        }
        return bean;
    }

    /**
     * 此方法的主要目标就是prop.getWriterMethod.invoke(bean,value)
     * @param bean
     * @param prop
     * @param value
     */
    protected void callSetter(Object bean,PropertyDescriptor prop, Object value) throws SQLException{

        Method method = prop.getWriteMethod();
        //没有对应的setter方法，或者setter方法参数超过1（不规范）
        if (method == null || method.getParameterCount() != 1) {
            return;
        }

        //下面的逻辑主要就是2点：
        // 1：值与参数的类型是兼容的吗？
        // 2:利用PropertyHandler体系对值进行额外的处理，
        // 比如说把数据库里面取出来的字符串数据转换成枚举

        try {
            Class<?> firstParam = method.getParameterTypes()[0];

            //应用PropertyHandler扩展机制

            for (PropertyHandler propertyHandler : propertyHandlers) {
                if (propertyHandler.support(firstParam, value)) {
                    value=propertyHandler.apply(firstParam,value);
                    break;
                }
            }
            if (isCompatibleType(value, firstParam)) {
                //兼容的话就可以赋值了
                method.invoke(bean, value);
            } else {
                throw new SQLException("值与属性类型不兼容，值得类型是：" + value.getClass() + " 属性的类型是:"+ firstParam);
            }
        }catch (IllegalArgumentException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (IllegalAccessException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());

        } catch (InvocationTargetException e) {
            throw new SQLException(
                    "Cannot set " + prop.getName() + ": " + e.getMessage());
        }




    }

    /**
     *目标：判断是否兼容主要是确定数据能不能正确赋值给对应的类型
     * 主要是靠Class类的isInstance方法来实现，
     * 这个方法就是instanceOf关键字的反射版本
     *  if (o instanceOf A){}
     *
     * 但是这个方法不能达到我们的要求，因为其对于value是null
     * 和目标类型是基本类型时都是返回false
     *
     * 因为null值是可以赋值给任何非基本类型（primitive）的属性
     * 而且实体类中的属性如果写成基本类型，那么你一个其对应的包装类型
     * 数据是可以赋值给它的
     *  int i
     *  Integer j = 100;
     *  i = j;
     * @param value
     * @param type
     * @return
     */
    private boolean isCompatibleType(Object value, Class<?> type) {

        if(type.isInstance(value)
                || matchesPrimitive(type,value.getClass())){
            return true;
        }
        return false;
    }

    /**
     * targetType是实体类的属性类型
     * valueType是实际的数据的类型
     * @param targetType
     * @param valueType
     * @return
     */
    private boolean matchesPrimitive(Class<?> targetType, Class<?> valueType) {
        if (targetType.isPrimitive() == false) {
            return false;
        }

        try {
            Field field = valueType.getField("TYPE");
            Object valuePrimitiveType = field.get(valueType);
            if (targetType == valuePrimitiveType) {
                return true;
            }
        } catch (NoSuchFieldException e) {
            //啥都不用干，没有这样字段的异常产生表示不是包装类型
            // lacking the TYPE field is a good sign that we're not working with a primitive wrapper.
            // we can't match for compatibility
        } catch (IllegalAccessException e) {
            // an inaccessible TYPE field is a good sign that we're not working with a primitive wrapper.
            // nothing to do.  we can't match for compatibility

        }
        return false;
    }
    protected int[] mapColumnsToProperties(ResultSetMetaData rsmd, PropertyDescriptor[] props) throws SQLException {
        int cols = rsmd.getColumnCount();
        int[] result = new int[cols + 1];
        Arrays.fill(result,PROPERTY_NOT_FOUND);

        for (int i = 1; i <= cols; i++) {
            String columnName = RsMetaUtils.getColumnName(rsmd,i);
            //默认是同名策略来查找对应的属性
            String propertyName = getMappingPropertyName(columnName);

            for (int j = 0; j < props.length; j++) {
                if (propertyName.equalsIgnoreCase(props[j].getName())) {
                    result[i] = j;
                    break;
                }
            }
        }

        return result;
    }

    /**
     * 默认情况下是进行同名映射，没有考虑大小写的问题
     * 字段username 属性的名字是userName
     *
     * 但是提供了一个扩展的机制，是靠Map来建立映射的规则
     * 这个map里面key是数据库字段查询的名字，
     * value是实体类的属性名（见测试类的queryBeanPropertyOverridesHandler）
     * @param columnName
     * @return
     */
    protected String getMappingPropertyName(String columnName) {
       String propertyName = propertyOverrides.get(columnName);
       return propertyName==null?columnName:propertyName;
    }
    private PropertyDescriptor[] propertyDescriptors(Class<?> clz) {
        BeanInfo beanInfo = null;
        try {
            beanInfo = Introspector.getBeanInfo(clz);
        } catch (IntrospectionException e) {
            e.printStackTrace();
        }
        return beanInfo.getPropertyDescriptors();
    }


    protected  <T> T createInstance(Class<?> clz) throws SQLException{
        T instance = null;
        try {
            instance =(T) clz.newInstance();
        } catch (InstantiationException e) {
            throw new SQLException("实例化出错，你的bean类有默认构造函数吗?");
        } catch (IllegalAccessException e) {
            throw new SQLException("实例化出错，你的bean类有默认构造函数吗?");
        }
        return instance;
    }

    @Deprecated
    public <T> T toBeanDeprecated(ResultSet rs,Class<?> clz) throws SQLException {
        T instance = null;
        try {
            instance =(T) clz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Field[] fields = clz.getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = rs.getObject(fieldName);
            try {
                field.set(instance,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

            field.setAccessible(false);
        }

        return instance;

    }
}
