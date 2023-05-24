package com.nf.dao;

import com.nf.entity.ProductEntity;

import java.util.List;

public interface ProductDao {
    List<ProductEntity> getLimit(String cid, String name, String status, int pageNo, int pageSize);

    List<ProductEntity> getAll();

    List<ProductEntity> getFuzzy(String name);

    int insert(ProductEntity product);
}
