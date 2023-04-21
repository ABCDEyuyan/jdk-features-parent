package mvcdemo.controller;

import com.nf.mvc.mapping.RequestMapping;
@RequestMapping("/prodUct")
public class ProductController {

    @RequestMapping("/list")
    public void m1(){
        System.out.println("m1 in proudct------");
    }
}
