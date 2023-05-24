package com.nf.dao.Impl;

import com.nf.dao.ProductDao;
import com.nf.dbutils.SqlExecutor;
import com.nf.dbutils.handlers.BeanListHandler;
import com.nf.entity.ProductEntity;
import com.nf.mvc.file.MultipartFile;
import com.nf.utils.DataSourceUtil;

import javax.sql.DataSource;
import java.io.Console;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @ClassName ProductDaoImpl
 * @Author ZL
 * @Date 2023/5/9 20:12
 * @Version 1.0
 * @Explain
 **/
public class ProductDaoImpl implements ProductDao {
    SqlExecutor sqlExecutor=new SqlExecutor(DataSourceUtil.getDataSource());
    @Override
    public List<ProductEntity> getLimit(String cid,String name,String status,int pageNo,int pageSize) {
        int no=(pageNo-1)*pageSize;
        int size=pageSize;

        if(status.isEmpty()){
            String sql="select id,name,image,price,count,status,cid from product where cid like ? and name like ? and status = '0' or status ='1' limit ?,?";
            return sqlExecutor.query(sql,new BeanListHandler<>(ProductEntity.class),"%"+cid+"%","%"+name+"%",status,no,size);
        }
        String sql="select id,name,image,price,count,status,cid from product where cid like ? and name like ? and status = ? limit ?,?";
        return sqlExecutor.query(sql,new BeanListHandler<>(ProductEntity.class),"%"+cid+"%","%"+name+"%",status,no,size);
    }

    @Override
    public List<ProductEntity> getAll() {
        String sql="select id,name,image,price,count,status,cid from product";
        return sqlExecutor.query(sql,new BeanListHandler<>(ProductEntity.class));
    }

    @Override
    public List<ProductEntity> getFuzzy(String name) {
        String sql="select id,name,image,price,count,status,cid from product where name like ?";
        return sqlExecutor.query(sql,new BeanListHandler<>(ProductEntity.class),"%"+name+"%");
    }

    @Override
    public int insert(ProductEntity product) {
        String sql="insert into product(name,image,price,count,status,cid) values(?,?,?,?,?,?)";
        return sqlExecutor.update(sql,product.getName(),product.getImage(),product.getPrice(),product.getCount(),product.getStatus(),product.getCid());
    }


}
