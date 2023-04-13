package ch05;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class Main {
    public static void main(String[] args) {
        List<Student> students = new ArrayList<>();
        students.add(new Student(1,"abc", 170, true));
        students.add(new Student(2,"de", 185, true));
        students.add(new Student(3,"asdfas", 155, false));
        students.add(new Student(4,"xx", 172, true));
        students.add(new Student(5,"uu", 178, true));
        students.add(new Student(6,"mmmmm", 186, true));
        students.add(new Student(6,"asdfad", 168, false));

        StudentManager manager = new StudentManager();
        List<Student> result = manager.process(students, new Predicate<Student>() {
            @Override
            public boolean test(Student student) {
                //return true;
                return student.getHeight()>175;
            }
        });






        List<Student> students1 = manager.process(students, s -> s.isGender() == true);
        //students1.forEach(s->System.out.println(s));
        students1.forEach(System.out::println);

        List<Student> students2 = manager.process(students,
                s -> s.isGender() == true && s.getHeight()>=175);
       // students2.forEach(s->System.out.println(s));

        List<Student> students3 = manager.process(students,
                s -> s.isGender() == false && s.getName().startsWith("a"));
        students3.forEach(s->System.out.println(s));

        List<Student> students4 = manager.process(students,
                s -> s.isGender() == true && s.getHeight()<=170);
        students4.forEach(s->System.out.println(s));
    }
}
