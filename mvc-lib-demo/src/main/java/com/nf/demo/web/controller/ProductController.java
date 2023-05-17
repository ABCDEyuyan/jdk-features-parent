package com.nf.demo.web.controller;


import com.nf.demo.entity.Pagination;
import com.nf.demo.entity.PaginationText;
import com.nf.demo.entity.ProductEntity;
import com.nf.demo.service.ProductService;
import com.nf.demo.service.impl.ProductServiceImpl;
import com.nf.demo.vo.PagedProductVO;
import com.nf.demo.vo.ResponseVO;
import com.nf.mvc.ViewResult;
import com.nf.mvc.argument.RequestParam;
import com.nf.mvc.file.MultipartFile;
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

    @RequestMapping("/insert")
    public ViewResult insert(MultipartFile pfile,ProductEntity product){
        product.setPfile(pfile);
        //UUID.randomUUID().toString()
        product.setImage(pfile.getOriginalFilename());
        productService.insert(product);
        return json(new ResponseVO(200, "ok", null));
    }

    @RequestMapping("/list/page")
    public ViewResult pagedList(boolean status,
                                @RequestParam(defaultValue = "1") int pageNo,
                                @RequestParam(defaultValue = "2") int pageSize){
        Long recordCount = productService.getPagedCount(status);
        Pagination pagination = new Pagination(pageSize,pageNo,recordCount);
        PaginationText paginationText = new PaginationText(pagination);
        List<ProductEntity> list = productService.getPagedAll(status, pagination);

        PagedProductVO pagedProductVO = new PagedProductVO(paginationText, list);
        return json(new ResponseVO(200, "ok", pagedProductVO));
    }
}
