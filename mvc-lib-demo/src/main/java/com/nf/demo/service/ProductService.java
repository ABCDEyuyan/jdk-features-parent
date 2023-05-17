package com.nf.demo.service;

import com.nf.demo.entity.Pagination;
import com.nf.demo.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    void insert(ProductEntity product);
    void update(ProductEntity product);
    List<ProductEntity> getAll();
    void delete(int id );
    List<ProductEntity> getPagedAll(boolean status, Pagination pagination);
    Long getPagedCount(boolean status);
}
