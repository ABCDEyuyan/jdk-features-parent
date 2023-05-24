package com.nf.service.Impl;

import com.nf.dao.Impl.ProductDaoImpl;
import com.nf.dao.ProductDao;
import com.nf.entity.ProductEntity;
import com.nf.mvc.file.MultipartFile;
import com.nf.service.Constant;
import com.nf.service.ProductService;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @ClassName ProductServiceImpl
 * @Author ZL
 * @Date 2023/5/9 23:01
 * @Version 1.0
 * @Explain
 **/
public class ProductServiceImpl implements ProductService {
    ProductDao dao=new ProductDaoImpl();
    @Override
    public List<ProductEntity> getLimit(String cid,String name,String status,int pageNo, int pageSize) {
        return dao.getLimit(cid,name,status,pageNo, pageSize);
    }

    @Override
    public List<ProductEntity> getAll() {
        return dao.getAll();
    }

    @Override
    public List<ProductEntity> getFuzzy(String name) {
        return dao.getFuzzy(name);
    }

    @Override
    public int insert(ProductEntity product) {
        MultipartFile multipartFile=product.getMultipartFile();
        if(multipartFile.isEmpty()){
            return 0;
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String localAddress = Constant.getLocalAddress();
        System.out.println("originalFilename = " + originalFilename);
        System.out.println("localAddress = " + localAddress);
        File dest=new File(localAddress+originalFilename);
        try {
            multipartFile.transferTo(dest);
        } catch (IOException e) {
            throw new RuntimeException(e.toString(),e);
        }
        return dao.insert(product);
    }
}
