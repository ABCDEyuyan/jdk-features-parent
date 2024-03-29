package mvcdemo.web.controller;

import com.nf.mvc.HandlerContext;
import com.nf.mvc.argument.PathVariable;
import com.nf.mvc.argument.RequestBody;
import com.nf.mvc.argument.RequestParam;
import com.nf.mvc.ioc.Injected;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.JsonViewResult;
import mvcdemo.MyConfigurationProperties1;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.nf.mvc.handler.HandlerHelper.json;

@RequestMapping("/product")
public class ProductController {

    @Injected
    private MyConfigurationProperties1 config1;
//list/2/5
    @RequestMapping("/list/{pageno}/{pagesize}")
    public JsonViewResult simple(@PathVariable("pageno") int pageNo,@PathVariable("pagesize") int pageSize){
        System.out.println("=========PathVariable测试============");
        System.out.println("pageNo = " + pageNo);
        System.out.println("pageSize = " + pageSize);

        return json(new ResponseVO(200,"ok",true));

    }

    @RequestMapping("/simple")
    public JsonViewResult simple(@RequestParam(defaultValue = "100")int id,@RequestParam(defaultValue = "abc",value = "username") String name){
        System.out.println("=========简单类型测试============");
        System.out.println("id = " + id);
        System.out.println("name = " + name);
        System.out.println("config1 = " + config1);
        System.out.println(this.hashCode());
        return new JsonViewResult(new ResponseVO(200,"ok",true));

    }

    //  http://localhost:8080/mvc/product/simple2?ids=1&ids=2&id2s=100&id2s=200&id3s=300&id3s=400
    @RequestMapping("/simple2")
    public JsonViewResult simple2(Integer[] ids, List<Integer> id2s,int[] id3s){
        System.out.println("===============简单类型数组，list测试============");
        System.out.println("ids.length = " + ids.length);
        System.out.println("ids = " + Arrays.toString(ids));
        System.out.println("id2s.size() = " + id2s.size());
        System.out.println("id3s.length = " + id3s.length);
        System.out.println("Arrays.toString(ids) = " + Arrays.toString(ids));
        id2s.forEach(System.out::println);
        System.out.println("Arrays.toString(id3s) = " + Arrays.toString(id3s));
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }

    @RequestMapping("/complex")
    public JsonViewResult complex( Emp2 emp){

        System.out.println("emp = " + emp);
        return new JsonViewResult(new ResponseVO(200,"ok",emp));
    }



    @RequestMapping("/body")
    public JsonViewResult body(@RequestBody Emp emp){
        System.out.println("====================RequestBody的参数解析器测试 pojo类==================");
        System.out.println("emp = " + emp);
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }


    @RequestMapping("/body2")
    public JsonViewResult body2(@RequestBody List<Emp> emps){
        System.out.println("====================RequestBody的参数解析器测试 List pojo类==================");

        //language=JSON
        String json = "[{\"id\":100,\"name\": \"abc\"},{\"id\": 200,\"name\": \"def\"\n" +
                "}]";
        System.out.println("emps.size() = " + emps.size());
        emps.forEach(System.out::println);
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }

    @RequestMapping("/body3")
    public JsonViewResult body3(@RequestBody Map<Integer,String> map){
        System.out.println("====================RequestBody的参数解析器测试map类==================");

     map.forEach((k,v)->{
         System.out.println(k);
         System.out.println(v);
     });
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }
    /**是不能像下面这样写的，第二个参数反序列化会爆请求流已关闭的错误，因为反序列化是从请求流中读取数据的 */
    @RequestMapping("/body4")
    public JsonViewResult body4(@RequestBody Emp emp,@RequestBody List<Emp> emps){
        System.out.println("emp = " + emp);
        System.out.println("emps.size() = " + emps.size());
        emps.forEach(System.out::println);
        return new JsonViewResult(new ResponseVO(200,"ok",true));
    }

    @RequestMapping("/ex")
    public JsonViewResult ex(){
        throw new ArithmeticException("1/0...");
    }
    @RequestMapping("/**")
    public void wildcardTest(){
        String method = HandlerContext.getContext().getRequest().getMethod();
        System.out.println("method = " + method);
        System.out.println("/** 的优先级测试------");
    }


    @RequestMapping("/api")
    public JsonViewResult api(HttpServletRequest req, HttpServletResponse resp, HttpSession session, ServletContext context){
        return new JsonViewResult(new ResponseVO(200,"api 解析器ok",true));
    }
}
