package com.nf.mvc;

import com.nf.mvc.view.PlainViewResult;
import com.nf.mvc.view.VoidViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ViewResult {
    public abstract void render(HttpServletRequest req, HttpServletResponse resp) throws Exception;

    /**
     * 这段代码在Adapter与ExceptionResolver里面都有使用，因为Adapter与ExceptionResolver本质上不想关
     * 所以这段代码放在两个里面的任意一个都不合适，放在这里更合理
     * <p>不放在HandlerHelper里是因为这个类主要是用户使用，用户基本用不到这个方法，会形成污染，<br/>
     * 把修饰符搞成默认修饰符，不让用户访问，但别的地方又访问不到
     * </p>
     *
     * @param handlerResult
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
