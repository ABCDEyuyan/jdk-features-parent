package com.nf.dbutils;

import com.nf.dbutils.handlers.*;
import org.junit.Test;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SqlExecutorTest {

    @Test
    public void update() throws SQLException {
        //使用try with resource语法帮我们自动调用connection的close方法关闭资源，
        // update方法本身没有做关闭Connection的动作
        try(Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/demo",
                "root", "root")) {
            SqlExecutor executor = new SqlExecutor();
            String sql = "delete from t5 where id = 1";
            executor.update(connection,  sql);

            //因为没要让update方法帮我们关（false），所以我们自己要关连接
            //connection.close();
        }
    }

    @Test
    public void updateAutoClose() throws SQLException {

        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                "root", "root");

        SqlExecutor executor = new SqlExecutor();
        String sql = "delete from t5 where id = 4";
        executor.update(connection , sql);

    }

    @Test
    public void query() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname from t5 ";

        ArrayHandler handler = new ArrayHandler();
        Object[] objects = executor.query(connection, sql, handler);

        System.out.println(Arrays.toString(objects));
    }


    @Test
    public void queryArrayListHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname from t5 ";

        ArrayListHandler handler = new ArrayListHandler();
        List<Object[]> objects = executor.query(connection, sql, handler);

        for (Object[] o : objects) {
            System.out.println(Arrays.toString(o));
        }

    }

    @Test
    public void queryScalarHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname from t5 ";

        ScalarHandler<String> handler = new ScalarHandler("uname");
        String name = executor.query(connection, sql, handler);

        System.out.println(name);
    }


    @Test
    public void queryMapHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname from t5 ";

        MapHandler handler = new MapHandler();
        Map<String,Object> result = executor.query(connection, sql, handler);

        for (Map.Entry<String, Object> entry : result.entrySet()) {
            System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
        }
    }


    @Test
    public void queryMapListHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname from t5 ";

        MapListHandler handler = new MapListHandler();
        List<Map<String,Object>> mapList = executor.query(connection, sql, handler);

        for (Map<String, Object> map : mapList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());
            }
            System.out.println("------------------");
        }


    }


    @Test
    public void queryKeyedMapHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname from t5 ";

        KeyedMapHandler<Integer> handler = new KeyedMapHandler();
        Map<Integer,Map<String,Object>> mapMap = executor.query(connection, sql, handler);

        for (Map.Entry<Integer, Map<String, Object>> map : mapMap.entrySet()) {
            System.out.println("外层key:" + map.getKey() );

            for (Map.Entry<String, Object> entry : map.getValue().entrySet()) {
                System.out.println("key:" + entry.getKey() + " value:" + entry.getValue());

            }
        }

    }


    @Test
    public void queryKeyedArrayHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname from t5 ";

        KeyedArrayHandler<String> handler = new KeyedArrayHandler("uname");
        Map<String,Object[]> mapMap = executor.query(connection, sql, handler);

        for (Map.Entry<String, Object[]> map : mapMap.entrySet()) {
            System.out.println("外层key:" + map.getKey() );

            Object[] objects = map.getValue();

            for (int i = 0; i < objects.length; i++) {
                System.out.println(objects[i]);
            }
            System.out.println("=============");
        }

    }

    @Test
    public void testNullToString(){
        String a = null;
        System.out.println(a);//输出null
    }

    @Test
    public void queryBeanPropertyFirstHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname,birthday,gender from t5 ";

        BeanPropertyFirstHandler<MyEntity> handler = new BeanPropertyFirstHandler(MyEntity.class);
        MyEntity entity = executor.query(connection, sql, handler);

        System.out.println(entity);

    }


    @Test
    public void queryBeanListPropertyFirstHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,birthday,gender from t5 ";

        BeanPropertyFirstListHandler<MyEntity> handler = new BeanPropertyFirstListHandler(MyEntity.class);
        List<MyEntity> entities = executor.query(connection, sql, handler);

        for (MyEntity entity : entities) {
            System.out.println(entity);
        }

    }


    @Test
    public void queryBeanHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname,birthday,gender from t5 ";

        BeanHandler<MyEntity> handler = new BeanHandler(MyEntity.class);
        MyEntity entity = executor.query(connection, sql, handler);

        System.out.println(entity);

    }

    @Test
    public void queryKeyedBeanHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname from t5 ";

        KeyedBeanHandler<BigInteger,MyEntity> handler = new KeyedBeanHandler(MyEntity.class);
        Map<BigInteger,MyEntity> mapMap = executor.query(connection, sql, handler);

        for (Map.Entry<BigInteger,MyEntity> entry : mapMap.entrySet()) {
            System.out.println("外层key:" + entry.getKey() );

            MyEntity entity = entry.getValue();

            System.out.println("entity = " + entity);
            System.out.println("=============");
        }

    }

    @Test
    public void queryBeanPropertyOverridesHandler() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "select id,uname,birthday,gender from t5 ";

        HashMap<String, String> mapping = new HashMap<>();
        mapping.put("uname", "username");

        RowProcessor rowProcessor = new DefaultRowProcessor(mapping);


        BeanHandler<MyEntity> handler = new BeanHandler(rowProcessor,MyEntity.class);
        MyEntity entity = executor.query(connection, sql, handler);

        System.out.println(entity);

    }

    @Test
    public void testInsert() throws SQLException {
        Connection connection = DriverManager
                .getConnection("jdbc:mysql://localhost:3306/demo",
                        "root", "root");

        SqlExecutor executor = new SqlExecutor();

        String sql = "insert into t5(uname) values(?) ";
        ScalarHandler<BigInteger> handler = new ScalarHandler<>();
        BigInteger id = executor.insert(connection,sql, handler, "auto");

        System.out.println(id);

    }

    @Test
    public void testType(){
        //包装类型的TYPE静态字段代表此包装类型对应的基础（Primitive）类型
        //java有8个基础类型：int，float。。。。
        Class<?> clz = Integer.TYPE;

//        Double.TYPE

       // Boolean.TYPE
    }

}