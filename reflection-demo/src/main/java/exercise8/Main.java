package exercise8;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
     Map<String, Object> map = new HashMap<>();
     map.put("id", 100);
     map.put("name","abc");
     Object o = mapToBean(map, A.class);
     System.out.println(o);

    }

    public static Object mapToBean(Map<String, Object> map, Class<?> clz) throws Exception {
        Object o = clz.newInstance();

        Field[] fields = clz.getDeclaredFields();

        for (Field field : fields) {
            String name = field.getName();
            field.setAccessible(true);
            field.set(o, map.get(name));
            field.setAccessible(false);
        }
        return o;

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