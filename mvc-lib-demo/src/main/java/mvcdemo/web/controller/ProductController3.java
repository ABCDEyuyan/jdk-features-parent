package mvcdemo.web.controller;

import com.nf.mvc.ViewResult;
import com.nf.mvc.mapping.RequestMapping;

import static com.nf.mvc.handler.HandlerHelper.json;


public class ProductController3 {

    @RequestMapping("/m1in3")
    public void m1(){
        System.out.println("m1 in proudct------");
    }

    @RequestMapping("/m2")
    public ViewResult m2(){
        return json(new ResponseVO(200,"ok","asdf"));
    }
}
