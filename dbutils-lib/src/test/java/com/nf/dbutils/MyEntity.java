package com.nf.dbutils;


import java.time.LocalDate;

public class MyEntity {
    private Integer id;

    private String uname;
    private LocalDate birthday;
    private Boolean gender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    @Override
    public String toString() {
        return "MyEntity{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                '}';
    }
}
