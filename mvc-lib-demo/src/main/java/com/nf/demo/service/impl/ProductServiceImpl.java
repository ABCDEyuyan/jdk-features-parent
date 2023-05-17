package com.nf.demo.service.impl;

import com.nf.demo.dao.ProductDao;
import com.nf.demo.dao.impl.ProductDaoImpl;
import com.nf.demo.entity.Pagination;
import com.nf.demo.entity.ProductEntity;
import com.nf.demo.service.ProductService;
import com.nf.mvc.file.MultipartFile;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    private ProductDao productDao = new ProductDaoImpl();
    @Override
    public void insert(ProductEntity product) {
        handleFile(product);
        productDao.insert(product);
    }

    @Override
    public void update(ProductEntity product) {
        handleFile(product);
        productDao.update(product);
    }

    @Override
    public List<ProductEntity> getAll() {
        return productDao.getAll();
    }

    @Override
    public void delete(int id) {
        productDao.delete(id);
    }

    @Override
    public List<ProductEntity> getPagedAll(boolean status, Pagination pagination) {
        return productDao.getPagedAll(status, pagination.getStart(), pagination.getCount());
    }

    @Override
    public Long getPagedCount(boolean status) {
        return productDao.getPagedCount(status);
    }

    private void handleFile(ProductEntity product){
        MultipartFile file = product.getPfile();
        if (file != null) {
            product.setImage(file.getOriginalFilename());
            try {
                file.transferTo(Paths.get("D:/tmp",file.getOriginalFilename()));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
