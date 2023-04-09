package com.nf.dbutils;

import java.util.Date;

public class MyEntity {
    private Integer id;

    private String username;
    private Date birthday;
    private Boolean gender;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
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
                ", uname='" + username + '\'' +
                ", birthday=" + birthday +
                ", gender=" + gender +
                '}';
    }
}
