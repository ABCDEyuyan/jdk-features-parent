package ch05;

public class Student {
    private Integer id;
    private String name;
    private double height;
    private  boolean gender;

    public Student() {
    }

    public Student(Integer id, String name, double height, boolean gender) {
        this.id = id;
        this.name = name;
        this.height = height;
        this.gender = gender;
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

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", height=" + height +
                ", gender=" + gender +
                '}';
    }
}
