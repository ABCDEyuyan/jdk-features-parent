package com.nf.dao;

import com.nf.entity.ProductEntity;
import com.nf.mvc.file.MultipartFile;

import java.util.List;

public interface ProductDao {
    List<ProductEntity> getLimit(String cid,String name,int pageNo,int pageSize);

    List<ProductEntity> getAll();

    List<ProductEntity> getFuzzy(String name);

    int insert(ProductEntity product);
}