package com.nf.web.controller;

import com.nf.entity.ProductEntity;
import com.nf.mvc.ViewResult;
import com.nf.mvc.argument.RequestBody;
import com.nf.mvc.argument.RequestParam;
import com.nf.mvc.file.MultipartFile;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.JsonViewResult;
import com.nf.service.Impl.ProductServiceImpl;
import com.nf.service.ProductService;
import com.nf.utils.vo.ResponseVo;

import java.util.List;

/**
 * @ClassName ProductController
 * @Author ZL
 * @Date 2023/5/9 23:04
 * @Version 1.0
 * @Explain
 **/
@RequestMapping("/product")
public class ProductController {
    ProductService service=new ProductServiceImpl();
    @RequestMapping("/limit")
    public JsonViewResult getLimit(@RequestParam(defaultValue = "") String cid,
                                   @RequestParam(defaultValue = "") String name,
                                   @RequestParam(defaultValue = "") String status,
                                   @RequestParam(defaultValue = "1") int pageNo,
                                   @RequestParam(defaultValue = "2") int pageSize){
        List<ProductEntity> result=service.getLimit(cid,name,status,pageNo, pageSize);
        return new JsonViewResult(new ResponseVo(200,"成功",result));
    }
    @RequestMapping("/list")
    public JsonViewResult getAll(){
        List<ProductEntity> result=service.getAll();
        return new JsonViewResult(new ResponseVo(200,"成功",result));
    }
    @RequestMapping("/insert")
    public JsonViewResult insert(@RequestBody MultipartFile multipartFile, ProductEntity product){
        product.setMultipartFile(multipartFile);
        int result=service.insert(product);
        return new JsonViewResult(new ResponseVo(200,"成功",result));
    }

    @RequestMapping("/fuzzy")
    public JsonViewResult getFuzzy(String name){
        List<ProductEntity> result=service.getFuzzy(name);
        return new JsonViewResult(new ResponseVo(200,"成功",result));
    }
}
