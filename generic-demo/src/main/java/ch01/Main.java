package ch01;

import java.util.ArrayList;

/**
 * 泛型的好处
 */
public class Main {
    public static void main(String[] args) {
        //1.编译时的类型检查
        ArrayList<Integer> al = new ArrayList<>();
        al.add(1);//只能添加数字
        Integer result = al.get(0);//没有类型转换
        //2.避免不必要的类型转换
        ArrayList al2 = new ArrayList();
        al2.add(1);
        al2.add("adfa");
        //还需要进行类型转换
        Integer o = (Integer) al2.get(0);//不报错
        Integer o2 = (Integer) al2.get(1);//报错

        //3. 编写通用的算法
  //比如只需要写一个类，ArrayList，HashMap之类就可以完成对任意类型数据的处理
        //如果没有这样的泛型技术，就只能一个一个类型重复编写，
        // 比如处理IntArrayList，DoubleArrayList
        //另外一种选择是只写一个类，但只能针对Object类型进行处理

    }
}
