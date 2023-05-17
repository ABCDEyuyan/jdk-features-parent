package com.nf.demo.dao;

import com.nf.demo.entity.ProductEntity;

import java.util.List;

public interface ProductDao {
    void insert(ProductEntity product);
    void update(ProductEntity product);
    List<ProductEntity> getAll();
    Long getPagedCount(boolean status);
    List<ProductEntity> getPagedAll(boolean status,int start, int count);
    void delete(int id );
}
