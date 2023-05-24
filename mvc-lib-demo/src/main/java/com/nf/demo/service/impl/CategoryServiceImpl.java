package com.nf.demo.service.impl;

import com.nf.demo.dao.CategoryDao;
import com.nf.demo.dao.impl.CategoryDaoImpl;
import com.nf.demo.entity.CategoryEntity;
import com.nf.demo.service.CategoryService;

import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private CategoryDao categoryDao = new CategoryDaoImpl();
    @Override
    public List<CategoryEntity> getAll(){
        return categoryDao.getAll();
    }
}
