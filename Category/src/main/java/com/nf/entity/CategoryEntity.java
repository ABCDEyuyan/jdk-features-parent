package com.nf.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @ClassName CategoryEntity
 * @Author ZL
 * @Date 2023/5/9 8:47
 * @Version 1.0
 * @Explain
 **/
public class CategoryEntity {
    private Integer cid;
    private String name;

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "CategoryEntity{" +
                "cid=" + cid +
                ", name='" + name + '\'' +
                '}';
    }
}
