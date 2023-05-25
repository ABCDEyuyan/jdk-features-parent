package test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) {
        //old();

        List<Student> students = getData();
        List<Student> students2 = getData();

        ClassInfo cl = new ClassInfo();
        cl.setId(185);
        cl.setName("java1ban");
        cl.setStudents(students);

        ClassInfo cl2 = new ClassInfo();
        cl2.setId(186);
        cl2.setName("java2ban");
        cl2.setStudents(students2);

        List<ClassInfo> classInfos = new ArrayList<>();
        classInfos.add(cl);
        classInfos.add(cl2);

      /*  Optional<Integer> integer1 = classInfos.stream()
                .flatMap(c -> c.getStudents().stream())
                .map(s -> s.getHeight())
                .reduce((i, j) -> i + j);
        System.out.println(integer1);*/


        Stream.of(1,2,3,4,5,6)
                .filter(s->{
                    System.out.println("filter-----:" + s);
                    return s>5;
                })
                .map(s->{
                    System.out.println("map-----:" + s);
                    return s;
                }) .forEach(System.out::println);


      /*  .sorted((s1,s2)->{
            System.out.println("sort----s1:" + s1 + "s2:" + s2);
            return s1.compareTo(s2);
        })*/
    }


    private static List<Student> getData(){
        List<Student> students = new ArrayList<>();
        Student s1 = new Student(1, "张三", true, 186);
        students.add(s1);

        Student s2 = new Student(2, "李四", true, 180);
        students.add(s2);

        Student s3 = new Student(3, "王五", true, 176);
        students.add(s3);

        Student s4 = new Student(4, "赵六", true, 170);
        students.add(s4);

        Student s5 = new Student(5, "钱六", false, 182);
        students.add(s5);
        return students;
    }
    private static void old() {
        List<Student> students = getData();


        List<Student> maleStudetns = new ArrayList<>();
        for (Student student : students) {
            if (student.isGender()) {
                maleStudetns.add(student);
            }
        }
        //降序排序
        maleStudetns.sort((x1,x2)->x2.getHeight()-x1.getHeight());
        List<Student> top3Students = new ArrayList<>();
        for (int i = 0; i < maleStudetns.size(); i++) {
            top3Students.add(maleStudetns.get(i));
            if(i>=2){
                break;
            }
        }

        for (Student student : top3Students) {
            System.out.println(student.getHeight());
        }
    }

}
