package ch10;

public class Student {
    private Integer id;
    private String name;
    private Double salary;
    private String deptName;

    public Student() {
    }

    public Student(Integer id, String name, Double salary, String deptName) {
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.deptName = deptName;
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

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", deptName='" + deptName + '\'' +
                '}';
    }
}
