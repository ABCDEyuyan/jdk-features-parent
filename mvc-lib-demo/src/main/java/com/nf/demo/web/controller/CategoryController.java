package com.nf.demo.web.controller;

import com.nf.demo.entity.CategoryEntity;
import com.nf.demo.service.CategoryService;
import com.nf.demo.service.impl.CategoryServiceImpl;
import com.nf.demo.vo.ResponseVO;
import com.nf.mvc.ViewResult;
import com.nf.mvc.mapping.RequestMapping;

import java.util.List;

import static com.nf.mvc.handler.HandlerHelper.json;

@RequestMapping("/category")
public class CategoryController {
    private CategoryService categoryService = new CategoryServiceImpl();
    @RequestMapping("/list")
    public ViewResult list(){
        List<CategoryEntity> list = categoryService.getAll();
        return json(new ResponseVO(200, "ok", list));
    }
}
