package mvcdemo.controller;

import com.nf.mvc.HandlerContext;
import com.nf.mvc.argument.RequestBody;
import com.nf.mvc.argument.RequestParam;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.JsonViewResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;

@RequestMapping("/prodUct")
public class ProductController {

    @RequestMapping("/list")
    public void m1(HttpServletRequest req){

        System.out.println("req.hashCode() = " + req.hashCode());
        System.out.println("HandlerContext.getContext().getRequest().hashCode() = " + HandlerContext.getContext().getRequest().hashCode());
        System.out.println("req.getMethod() = " + req.getMethod());
        System.out.println("m1 in proudct------");
    }

    @RequestMapping("/list2")
    public void m2(){
        String method = HandlerContext.getContext().getRequest().getMethod();

        System.out.println("method = " + method);

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

//http://localhost:8080/mvc/product/delete?ids=1&ids=2&id2s=100&id2s=200&id=123&name=abc
    @RequestMapping("/delete")
    public JsonViewResult delete(Integer[] ids, List<Integer> id2s,Emp emp){
        System.out.println("====delete in product-");
        System.out.println("ids.length = " + ids.length);
        System.out.println("ids = " + Arrays.toString(ids));
        System.out.println("id2s.size() = " + id2s.size());
        System.out.println("emp = " + emp);
        id2s.forEach(System.out::println);
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }

    @RequestMapping("/page")
    public JsonViewResult page(@RequestParam("no") int pageNo,
                                 @RequestParam(defaultValue = "5") String pageSize){

        System.out.println("pageNo = " + pageNo);
        System.out.println("pageSize = " + pageSize);
        return new JsonViewResult(new ResponseVO(200,"ok",true));


    }

    @RequestMapping("/json")
    public JsonViewResult json(@RequestBody Emp emp){

        System.out.println("emp = " + emp);
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }


    @RequestMapping("/json2")
    public JsonViewResult json2(@RequestBody List<Emp> emps){
        //language=JSON
        String json = "[{\"id\":100,\"name\": \"abc\"},{\"id\": 200,\"name\": \"def\"\n" +
                "}]";
        System.out.println("emps.size() = " + emps.size());
        emps.forEach(System.out::println);
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }

    /**因为反序列化是从请求流中读取数据的 */
    @RequestMapping("/json3")
    public JsonViewResult json3(@RequestBody Emp emp,@RequestBody List<Emp> emps){
        System.out.println("emp = " + emp);
        System.out.println("emps.size() = " + emps.size());
        emps.forEach(System.out::println);
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }
}
