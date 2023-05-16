package com.nf.demo.service.impl;

import com.nf.demo.dao.ProductDao;
import com.nf.demo.dao.impl.ProductDaoImpl;
import com.nf.demo.entity.ProductEntity;
import com.nf.demo.service.ProductService;

import java.util.List;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao = new ProductDaoImpl();
    @Override
    public void insert(ProductEntity product) {

    }

    @Override
    public void update(ProductEntity product) {

    }

    @Override
    public List<ProductEntity> getAll() {
        return productDao.getAll();
    }

    @Override
    public void delete(int id) {

    }
}
