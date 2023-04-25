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


    @RequestMapping("/delete")
    public JsonViewResult delete(int[] ids){
        System.out.println("====delete in product-");
        System.out.println("ids = " + ids);
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }
}
