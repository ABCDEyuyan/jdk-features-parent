package com.nf.demo.web.controller;


import com.nf.demo.entity.Pagination;
import com.nf.demo.entity.PaginationText;
import com.nf.demo.entity.ProductEntity;
import com.nf.demo.service.ProductService;
import com.nf.demo.service.impl.ProductServiceImpl;
import com.nf.demo.vo.PagedProductVO;
import com.nf.mvc.ViewResult;
import com.nf.mvc.argument.RequestParam;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.support.vo.ResponseVO;

import java.util.List;

import static com.nf.mvc.handler.HandlerHelper.json;

@RequestMapping("/product")
public class ProductController {
    private ProductService productService = new ProductServiceImpl();

    @RequestMapping("/list")
    public ViewResult list() {
        List<ProductEntity> list = productService.getAll();
        return json(new ResponseVO(200, "ok", list));
    }

    @RequestMapping("/list/page")
    public ViewResult pagedList(boolean status,
                                @RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "2") int pageSize) {
        Long recordCount = productService.getPagedCount(status);
        Pagination pagination = new Pagination(pageSize, pageNo, recordCount);
        PaginationText paginationText = new PaginationText(pagination);
        List<ProductEntity> list = productService.getPagedAll(status, pagination);

        PagedProductVO pagedProductVO = new PagedProductVO(paginationText, list);
       // return json(new ResponseVO(200, "ok", pagedProductVO));
        return json(ResponseVO.success(pagedProductVO));
    }

    @RequestMapping("/insert")
    public ViewResult insert( ProductEntity product) {
        productService.insert(product);
        return json(new ResponseVO(200, "ok", null));
    }

    @RequestMapping("/delete")
    public ViewResult delete(int id) {
        productService.delete(id);
        return json(new ResponseVO(200, "ok", null));
    }

    @RequestMapping("/update")
    public ViewResult update(ProductEntity product) {
        productService.update(product);
        return json(new ResponseVO(200, "ok", null));
    }
}
