package com.nf.mvc.sort;

import com.nf.mvc.support.OrderComparator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortTest {
    @Test
    public void testSort(){
        Student s1 = new Student(1, "b", 165);
        Student s2 = new Student(2, "c", 185);
        Student s3 = new Student(3, "a", 170);
        Student s4 = new Student(4, "d", 190);

        List<Student> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);

        //实现Comparable接口，表示自己知道怎么比较，只能是一种实现
        //实现Comparator接口，可以实现多个，这样就可以有很多种不同的比较方法
        //自己放在减号的左边，就是升序
       // Collections.sort(list);

       Collections.sort(list,new HeightComparator());
        //sort实现的排序是稳定的（见源码的说明，好像是归并排序的算法）
        //因为名字长度一样，所以排序算法是否稳定就决定顺序
       // Collections.sort(list,new NameComparator());
        list.forEach(System.out::println);
    }



    @Test
    public void testOrderSort(){
        StudentInf s1 = new Student1(1, "b", 165);
        StudentInf s2 = new Student2(2, "c", 185);


        List<StudentInf> list = new ArrayList<>();
        list.add(s1);
        list.add(s2);



        Collections.sort(list,new OrderComparator<>());

        list.forEach(System.out::println);
    }
}
