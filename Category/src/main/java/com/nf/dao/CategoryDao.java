package com.nf.dao;

import com.nf.entity.CategoryEntity;

import java.math.BigInteger;
import java.util.List;

public interface CategoryDao {
    List<CategoryEntity> getList();
    int deleteById(Integer cid);

    int insert(String name);

    int update(int id,String name);
}
