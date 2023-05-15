package com.nf.mvc;

import com.nf.mvc.view.PlainViewResult;
import com.nf.mvc.view.VoidViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 此类用来封装Handler执行的结果，通常用户的控制器方法会返回此类型，不同的实现通常代表不同的响应情况，
 * 比如用户返回一个{@link com.nf.mvc.view.JsonViewResult},那么就会响应一个json数据给用户。
 * 用户代码中不需要直接实例化某一个具体的ViewResult子类实例，可以借助于{@link com.nf.mvc.handler.HandlerHelper}
 * 类中的静态方法，常见代码如下
 * <pre class="code">
 *   &#064;RequestMapping("/json")
 *   public ViewResult json(...){
 *       //省略逻辑代码
 *       return json(new ResponseVO(...));
 *   }
 * </pre>
 * @see com.nf.mvc.view.JsonViewResult
 * @see PlainViewResult
 * @see com.nf.mvc.view.ForwardViewResult
 * @see com.nf.mvc.view.RedirectViewResult
 * @see com.nf.mvc.view.HtmlViewResult
 * @see VoidViewResult
 */
public abstract class ViewResult {
    public abstract void render(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 这段代码在Adapter与ExceptionResolver里面都有使用，因为Adapter与ExceptionResolver本质上不相关
     * 所以这段代码放在两个里面的任意一个都不合适，放在这里更合理
     * <p>不放在HandlerHelper里是因为这个类主要是用户使用，用户基本用不到这个方法，会形成污染，<br/>
     * 把修饰符搞成默认修饰符让用户看不到，但这样别的地方又访问不到，所以就把此方法放到了这里
     * </p>
     * @param handlerResult: handler执行之后的结果，可能是null，ViewResult或者别的类型，详见{@link HandlerAdapter}
     * @return
     */
    public static ViewResult adaptHandlerResult(Object handlerResult) {
        ViewResult viewResult;
        if (handlerResult == null) {
            /* 这种情况表示handler方法执行返回null或者方法的签名本身就是返回void，
             反射调用一个void类型的方法时，invoke方法的返回值是null*/
            viewResult = new VoidViewResult();
        } else if (handlerResult instanceof ViewResult) {
            viewResult = (ViewResult) handlerResult;
        } else {
            viewResult = new PlainViewResult(handlerResult.toString());
        }

        return viewResult;
    }
}
