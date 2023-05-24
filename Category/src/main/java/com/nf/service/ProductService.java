package com.nf.service;

import com.nf.entity.ProductEntity;
import com.nf.mvc.file.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductEntity> getLimit(String cid,String name,String status,int pageNo,int pageSize);

    List<ProductEntity> getAll();

    List<ProductEntity> getFuzzy(String name);

    int insert(ProductEntity product);
}
