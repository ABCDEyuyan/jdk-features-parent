package mvcdemo.controller;

import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.JsonViewResult;

@RequestMapping("/prodUct")
public class ProductController {

    @RequestMapping("/list")
    public void m1(){
        System.out.println("m1 in proudct------");
    }

    @RequestMapping("/insert")
    public JsonViewResult insert(int id,String name){
        //调用业务类，完成数据的插入
        //ProductServiceImpl.insert(id,name)

        System.out.println("====insert in product-");
        System.out.println("id = " + id);
        System.out.println("name = " + name);

        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }
}
