package exercise5;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) throws Exception {

        Dept dept = new Dept();
        dept.setId(1000);
        dept.setDeptname("abc");

        insert(dept);
    }

    public static void insert(Dept a ) throws Exception {
        //insert into dept(id,deptname) values(?,?)
        StringBuilder builder = new StringBuilder("insert into ");
        String tableName = a.getClass().getSimpleName();
        builder.append(tableName);
        builder.append("(");

        Field[] fields = a.getClass().getDeclaredFields();

        for (Field field : fields) {
            String fieldname = field.getName();
            builder.append(fieldname);
            builder.append(",");
        }
        //去掉最后一个逗号
        builder.deleteCharAt( builder.length() -1);

        builder.append(") values(");

        for (int i = 0; i < fields.length; i++) {
            builder.append("?");
            builder.append(",");
        }
        builder.deleteCharAt( builder.length() -1);
        builder.append(")");

        String sql = builder.toString();
        System.out.println(sql);

        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo", "root", "root");

        PreparedStatement pstmt = connection.prepareStatement(sql);

        //处理？值得问题
        int i = 1;
        for (Field field : fields) {
            String fieldname = field.getName();
            field.setAccessible(true);
            Object value = field.get(a);
            field.setAccessible(false);

            pstmt.setObject(i,value);
            i++;
        }

        pstmt.executeUpdate();

    }

}


class Dept{
    private int id;

    private String deptname;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeptname() {
        return deptname;
    }

    public void setDeptname(String deptname) {
        this.deptname = deptname;
    }

    @Override
    public String toString() {
        return "Dept{" +
                "id=" + id +
                ", deptname='" + deptname + '\'' +
                '}';
    }
}

