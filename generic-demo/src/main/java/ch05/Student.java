package ch05;

public class Student implements Comparable<Student>{
    private Integer id;
    private String name;
    private int height;

    public Student() {
    }

    public Student(Integer id, String name, int height) {
        this.id = id;
        this.name = name;
        this.height = height;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", height=" + height +
                '}';
    }

    @Override
    public int compareTo(Student o) {
        //return this.name.compareTo(o.name);
        return this.height-o.height;
    }
}
