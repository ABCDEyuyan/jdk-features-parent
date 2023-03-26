package exercise7;

import java.lang.reflect.Field;

public class Main {
    public static void main(String[] args) {
        A a = new A();
        a.setId(100);
        a.setName("abc");
        B b = (B) copyBean(a,B.class);

        System.out.println(b);

    }

    public  static Object copyBean(Object source,Class<?> target){
        Object o = null;
        try {
            //创建出目标类的一个对象出来
            o = target.newInstance();
            //以目标字段为准
            Field[] fields = target.getDeclaredFields();
            for (Field field : fields) {
                String fieldName = field.getName();
                //获取同名的字段  也就是source对象的某个字段的值
                Field sourceField = source.getClass().getDeclaredField(fieldName);
                sourceField.setAccessible(true);
                Object sourceValue = sourceField.get(source);
                sourceField.setAccessible(false);

                //给目标字段赋值
                field.setAccessible(true);
                field.set(o,sourceValue);
                field.setAccessible(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  o;
    }
}

class A{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "A{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

class B{
    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "B{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}