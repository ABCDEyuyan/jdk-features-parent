package com.nf.mvc.builder;

import java.util.Objects;

public class Student {

    private int id;
    private String name;
    private boolean gender;
    private  int height;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void  setName(String name) {
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



    public static void main(String[] args) {
        /*StudentBuilder builder = new StudentBuilder();
        builder
                .id(100)
                .name("abc")
                .gender(true)
                .height(180);
        Student student = builder.build();
        System.out.println("student = " + student);*/

        Student student = new StudentBuilder().id(100).name("d").gender(false).height(190).build();
        System.out.println(student);

    }

    public static final class StudentBuilder {
        private int id;
        private String name;
        private boolean gender;
        private  int height;


        public StudentBuilder id(int id){
            this.id = id;
            return  this;
        }

        public StudentBuilder name(String name){
            this.name = name;
            return  this;
        }

        public StudentBuilder gender(boolean gender){
            this.gender = gender;
            return  this;
        }

        public StudentBuilder height(int height){
            this.height = height;
            return  this;
        }

        public Student build(){
            Student s = new Student();
            s.setId(id);
            s.setName(name);
            s.setGender(gender);
            s.setHeight(height);

            return s;
        }

    }

}
