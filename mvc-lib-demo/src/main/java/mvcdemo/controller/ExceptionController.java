package mvcdemo.controller;

import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.JsonViewResult;

public class ExceptionController {
    @RequestMapping("/ex")
    public JsonViewResult suan(int a,int b){
        int result = a/b;
        return new JsonViewResult(new ResponseVO(200,"ok",result));
    }
}
