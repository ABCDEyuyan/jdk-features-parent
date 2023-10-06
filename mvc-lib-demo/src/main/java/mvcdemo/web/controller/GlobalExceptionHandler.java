package mvcdemo.web.controller;

import com.nf.mvc.exception.ExceptionHandler;
import com.nf.mvc.exception.MultiExceptionHandler;
import com.nf.mvc.view.JsonViewResult;

public class GlobalExceptionHandler {
   /* @ExceptionHandler(RuntimeException.class)
    public JsonViewResult handleRuntime(RuntimeException re){
        //System.out.println("re = " + re);
        return new JsonViewResult(new ResponseVO(10001,"运行异常:" + re.getMessage(),"runtime--"));
    }

    @ExceptionHandler(ArithmeticException.class)
    public JsonViewResult handleArithmeticException(ArithmeticException re){
       // System.out.println("suan shu re = " + re);
        return new JsonViewResult(new ResponseVO(10002,"算术异常:" + re.getMessage(),"算数--"));
    }
*/
   @MultiExceptionHandler(ArithmeticException.class)
   public JsonViewResult handleArithmeticException(String a,ArithmeticException re){
       // System.out.println("suan shu re = " + re);
       System.out.println("异常处理方法参数a = " + a);
       return new JsonViewResult(new ResponseVO(10002,"算术异常:" + re.getMessage(),"算数--"));
   }
}
