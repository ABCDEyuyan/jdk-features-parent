package com.nf.service.Impl;

import com.nf.dao.CategoryDao;
import com.nf.dao.Impl.CategoryDaoImpl;
import com.nf.entity.CategoryEntity;
import com.nf.service.CategoryService;

import java.math.BigInteger;
import java.util.List;

/**
 * @ClassName CategoryServiceImpl
 * @Author ZL
 * @Date 2023/5/9 8:53
 * @Version 1.0
 * @Explain
 **/
public class CategoryServiceImpl implements CategoryService {
    CategoryDao dao=new CategoryDaoImpl();
    @Override
    public List<CategoryEntity> getList() {
        return dao.getList();
    }

    @Override
    public int deleteById(Integer cid) {
        return dao.deleteById(cid);
    }

    @Override
    public int insert(String name) {
        return dao.insert(name);
    }

    @Override
    public int update(int id,String name) {
        return dao.update(id,name);
    }
}
