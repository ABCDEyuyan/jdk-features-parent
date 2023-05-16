package com.nf.demo.web.controller;


import com.nf.demo.entity.ProductEntity;
import com.nf.demo.service.ProductService;
import com.nf.demo.service.impl.ProductServiceImpl;
import com.nf.demo.vo.ResponseVO;
import com.nf.mvc.ViewResult;
import com.nf.mvc.mapping.RequestMapping;

import java.util.List;

import static com.nf.mvc.handler.HandlerHelper.json;

@RequestMapping("/product")
public class ProductController {
    private ProductService productService = new ProductServiceImpl();
    @RequestMapping("/list")
    public ViewResult list(){

        List<ProductEntity> list = productService.getAll();
        return json(new ResponseVO(200,"ok",list));
    }
}
