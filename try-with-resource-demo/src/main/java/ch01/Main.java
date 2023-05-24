package ch01;

import java.sql.Connection;

/**
 * try with resource
 * 自动的帮我们调用close方法，
 * 而close方法我们一般用来做资源清理操作
 *
 * 等价于
 * try
 * {
 *
 * }finally{
 *     xx.close()
 * }
 */
public class Main {
    public static void main(String[] args) throws Exception {

        //写法一，方法有异常抛出，还是会抛出异常，没有捕获
        try (MySome some = new MySome()) {
            //try这个代码块里面如果出了异常，也会调用close
            some.doSth();
        }

        //写法二：已经有一个对象，就需要在try括号里，声明一个新的变量并赋值
        MySome some2 = new MySome();
        try (MySome s2 = some2) {

        }

        //写法三：
        try (MySome s3 = new MySome(); Connection con = getConnection()) {

        }


    }

    private static Connection getConnection(){
        return null;
    }
}
