package com.nf.service;

import com.nf.entity.CategoryEntity;

import java.math.BigInteger;
import java.util.List;

public interface CategoryService {
    List<CategoryEntity> getList();
    int deleteById(Integer cid);

    int insert(String name);

    int update(int cid,String name);

}
