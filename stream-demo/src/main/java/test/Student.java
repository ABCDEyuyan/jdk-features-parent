package test;


public class Student {
    private int id;
    private String name;
    private boolean gender;
    private  int height;

    public Student() {
    }

    public Student(int id, String name, boolean gender, int height) {
        this.id = id;
        this.name = name;
        this.gender = gender;
        this.height = height;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
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
                ", gender=" + gender +
                ", height=" + height +
                '}';
    }
}
