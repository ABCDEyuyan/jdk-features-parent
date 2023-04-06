package com.nf.dbutils;

public class MyEntity {
    private Integer id;
    private String uname;

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

    @Override
    public String toString() {
        return "MyEntity{" +
                "id=" + id +
                ", uname='" + uname + '\'' +
                '}';
    }
}
