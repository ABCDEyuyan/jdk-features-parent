package com.nf.dao.Impl;

import com.nf.dao.CategoryDao;
import com.nf.dbutils.SqlExecutor;
import com.nf.dbutils.handlers.BeanHandler;
import com.nf.dbutils.handlers.BeanListHandler;
import com.nf.dbutils.handlers.ScalarHandler;
import com.nf.entity.CategoryEntity;
import com.nf.utils.DataSourceUtil;

import java.math.BigInteger;
import java.util.List;

/**
 * @ClassName CategoryDaoImpl
 * @Author ZL
 * @Date 2023/5/9 8:47
 * @Version 1.0
 * @Explain
 **/
public class CategoryDaoImpl implements CategoryDao {
    SqlExecutor sqlExecutor=new SqlExecutor(DataSourceUtil.getDataSource());

    @Override
    public List<CategoryEntity> getList() {
        String sql="select cid,name from category";
        return sqlExecutor.query(sql,new BeanListHandler<>(CategoryEntity.class));
    }

    @Override
    public int deleteById(Integer cid) {
        String sql = "delete from category where cid = ?";
        return sqlExecutor.update(sql,cid);
    }

    @Override
    public int insert(String name) {
        String sql = "insert into category(name) values(?)";
        return sqlExecutor.update(sql,name);
    }

    @Override
    public int update(int cid,String name) {
        String sql = "update category set name = ? where cid = ?";
        return sqlExecutor.update(sql,name,cid);
    }


}
