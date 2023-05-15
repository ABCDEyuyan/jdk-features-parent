package com.nf.entity;

/**
 * @ClassName UserEntity
 * @Author ZL
 * @Date 2023/5/12 9:14
 * @Version 1.0
 * @Explain
 **/
public class UserEntity {
    private String name;
    private String password;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
