package com.nf.web.controller;

import com.nf.entity.CategoryEntity;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.JsonViewResult;
import com.nf.service.CategoryService;
import com.nf.service.Impl.CategoryServiceImpl;
import com.nf.utils.vo.ResponseVo;

import java.util.List;

/**
 * @ClassName CategoryController
 * @Author ZL
 * @Date 2023/5/9 8:55
 * @Version 1.0
 * @Explain
 **/
@RequestMapping("/category")
public class CategoryController {
    CategoryService service=new CategoryServiceImpl();
    @RequestMapping("/list")
    public JsonViewResult categoryQuery(){

        List<CategoryEntity> result=service.getList();
        return new JsonViewResult(new ResponseVo(200,"成功",result));
    }

    @RequestMapping("/insert")
    public JsonViewResult insert(String name) {
        int result = service.insert(name);
        return new JsonViewResult(new ResponseVo(200, "ok", result));
    }

    @RequestMapping("/update")
    public JsonViewResult update(int cid,String name) {
        int update = service.update(cid,name);
        return new JsonViewResult(new ResponseVo(200, "ok", update));
    }

    @RequestMapping("/delete")
    public JsonViewResult delete(Integer cid) {
        int update = service.deleteById(cid);
        return new JsonViewResult(new ResponseVo(200, "ok", update));
    }
}
