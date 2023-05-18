package ch05.exercise;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        Dept dept1 = new Dept(1, "人事部");
        Dept dept2 = new Dept(2, "研发部");


        Emp emp1 = new Emp(1, "a1", 1000);
        Emp emp2 = new Emp(2, "a2", 2000);
        Emp emp3 = new Emp(3, "a3", 10000);
        Emp emp4 = new Emp(4, "a4", 6000);
        Emp emp5 = new Emp(5, "a5", 7000);
        Emp emp6 = new Emp(6, "a6", 8000);


        List<Emp> dept1Emps = new ArrayList<>();
        dept1Emps.add(emp1);
        dept1Emps.add(emp2);
        dept1Emps.add(emp3);


        List<Emp> dept2Emps = new ArrayList<>();
        dept2Emps.add(emp4);
        dept2Emps.add(emp5);
        dept2Emps.add(emp6);

        dept1.setEmps(dept1Emps);
        dept2.setEmps(dept2Emps);

//基于这个部门的流，输出所有员工的工资
      /*  Stream<Emp> empStream = Stream.of(dept1, dept2)
                .flatMap(d -> d.getEmps().stream());
        empStream.map(e->e.getSalary())
                .forEach(System.out::println);*/
//上面的写法等价于下面的写法
        Stream.of(dept1, dept2)
                .flatMap(d -> d.getEmps().stream())
                .map(e -> e.getSalary())
                .forEach(System.out::println);
//flatMapToInt,FlatMapToDouble,FlatMapToLong
    }
}
