package com.nf.demo.dao.impl;

import com.nf.dbutils.SqlExecutor;
import com.nf.dbutils.handlers.BeanListHandler;
import com.nf.dbutils.handlers.ScalarHandler;
import com.nf.demo.dao.ProductDao;
import com.nf.demo.entity.ProductEntity;
import com.nf.demo.util.SqlExecutorUtils;

import java.util.List;

public class ProductDaoImpl implements ProductDao {
    SqlExecutor executor = SqlExecutorUtils.getExecutor();
    @Override
    public void insert(ProductEntity product) {
        String sql = "insert into product (pname,price,image,qty,status,pubdate,cid) values(?,?,?,?,?,?,?)";
        executor.update(sql, product.getPname(),
                product.getPrice(), product.getImage(),
                product.getQty(), product.getStatus(),
                product.getPubdate(), product.getCid());
    }

    @Override
    public void update(ProductEntity product) {
        String sql = "update product set pname=?,price=?,image=?,qty=?,status=?,pubdate=?,cid=? where id = ?";
        executor.update(sql, product.getPname(), product.getPrice(),
                product.getImage(), product.getQty(), product.getStatus(),
                product.getPubdate(), product.getCid(), product.getId());
    }

    @Override
    public List<ProductEntity> getAll() {
        String sql = "select id,pname,image,price,status,pubdate,qty,cid from product";
        return executor.query(sql,new BeanListHandler<>(ProductEntity.class));
    }

    @Override
    public void delete(int id) {
        String sql = "delete from product where id = ?";
        executor.update(sql, id);
    }

    @Override
    public List<ProductEntity> getPagedAll(boolean status, int start, int count) {

        String sql = "select id,pname,image,price,status,pubdate,qty,cid from product where status=? limit ?,?";
        return executor.query(sql,new BeanListHandler<>(ProductEntity.class),status,start,count);
    }

    @Override
    public Long getPagedCount(boolean status) {
        String sql = "select count(*) from product where status=?";
        return executor.query(sql, new ScalarHandler<>(), status);
    }
}
