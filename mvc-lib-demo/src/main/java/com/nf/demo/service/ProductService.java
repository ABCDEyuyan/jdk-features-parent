package com.nf.demo.service;

import com.nf.demo.entity.ProductEntity;

import java.util.List;

public interface ProductService {
    void insert(ProductEntity product);
    void update(ProductEntity product);
    List<ProductEntity> getAll();
    void delete(int id );
}
