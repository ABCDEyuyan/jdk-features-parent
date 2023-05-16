package com.nf.demo.dao.impl;

import com.nf.dbutils.SqlExecutor;
import com.nf.dbutils.handlers.BeanListHandler;
import com.nf.demo.dao.CategoryDao;
import com.nf.demo.entity.CategoryEntity;
import com.nf.demo.util.SqlExecutorUtils;

import java.util.List;

public class CategoryDaoImpl implements CategoryDao {
    private SqlExecutor executor = SqlExecutorUtils.getExecutor();
    @Override
    public List<CategoryEntity> getAll() {
        String sql = "select id,cname from category";
        return executor.query(sql, new BeanListHandler<>(CategoryEntity.class));
    }
}
