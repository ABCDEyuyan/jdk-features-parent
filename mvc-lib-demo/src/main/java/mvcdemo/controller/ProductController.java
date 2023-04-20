package mvcdemo.controller;

import com.nf.mvc.mappings.RequestMapping;
@RequestMapping("/prodUct")
public class ProductController {

    @RequestMapping("/list")
    public void m1(){
        System.out.println("m1 in proudct------");
    }
}
