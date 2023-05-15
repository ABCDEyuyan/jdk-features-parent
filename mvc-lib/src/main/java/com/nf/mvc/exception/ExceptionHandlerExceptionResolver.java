package com.nf.mvc.exception;

import com.nf.mvc.HandlerExceptionResolver;
import com.nf.mvc.MvcContext;
import com.nf.mvc.ViewResult;
import com.nf.mvc.handler.HandlerMethod;
import com.nf.mvc.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

import static com.nf.mvc.ViewResult.adaptHandlerResult;
import static com.nf.mvc.util.ExceptionUtils.getRootCause;

/**
 * 这是mvc框架主用的一个异常解析器，此异常解析器支持用ExceptionHandler注解.
 * <p>
 *  用户通常编写一个单独的类，此类里面每一个方法用来处理某一个异常，代码如下：
 *  <pre class="code">
 *      public class GlobalExceptionHandler{
 *          &#064;ExceptionHandler(RuntimeException.class)
 *          public JsonViewResult handleRuntime(RuntimeException re){
 *               System.out.println("re = " + re);
 *              return new JsonViewResult(new ResponseVO(10001,"订单超时。。","runtime--"));
 *          }
 *
 *          &#064;ExceptionHandler(ArithmeticException.class)
 *          public JsonViewResult handleArithmeticException(ArithmeticException re){
 *              System.out.println("suan shu re = " + re);
 *              return new JsonViewResult(new ResponseVO(10002,"sdfa","算数--"));
 *          }
 *      }
 *  </pre>
 * </p>
 *
 * <p>
 *     如果一个异常有多个异常处理方法都是可以处理的，那么会用方法参数的类型最接近(指的是继承链条上)引发的异常的方法来处理异常，
 *     比如引发了一个ArithmeticException，那么上面的示例代码中handleArithmeticException方法会被用来处理异常，
 *     如果类中没有能处理ArithmeticException异常的方法，那么就会交给handleRuntime方法去处理
 * </p>
 * <p>
 *     这些异常处理方法必须有一个参数，并且类型只能是Exception类型或其子类型，
 *     其方法返回值与handler的方法返回值是一样的，可以是void，ViewResult或其它类型，
 *     异常解析器负责把其都适配为ViewResult类型
 * </p>
 */
public class ExceptionHandlerExceptionResolver implements HandlerExceptionResolver {
    //这个字段可以设计为protected，但必要性不是特别大，所以还是保留private的设计，遵循能不暴露就不暴露的原则
    private List<HandlerMethod> exHandleMethods = new ArrayList<>();

    public ExceptionHandlerExceptionResolver() {
        fetchExHandlMethods();
    }

    @Override
    public ViewResult resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        Object handleResult;
        Exception exposedException = (Exception) getRootCause(ex);
        for (HandlerMethod handlerMethod : exHandleMethods) {
            Method method = handlerMethod.getHandlerMethod();
            Class<?> exceptionClass = method.getDeclaredAnnotation(ExceptionHandler.class).value();
            if (exceptionClass.isAssignableFrom(exposedException.getClass())) {
                try {
                    Object instance = ReflectionUtils.newInstance(method.getDeclaringClass());
                    /*从这里可以看出，我们的全局异常处理只能有一个参数，而且必须有,参数的类型就是异常*/
                    handleResult = method.invoke(instance, exposedException);
                    return adaptHandlerResult(handleResult);
                } catch (Exception e) {
                    /* 进入到这里就是异常处理方法本身的执行出了错，catch里什么都不干，相当于吞掉异常处理方法本身的异常，
                    异常处理方法无法正确的处理异常，也就是说本异常解析器无法处理异常，这样，通过返回null的形式，
                    就继续交给下一个异常解析器去处理，下一个异常解析器处理的仍然是最开始抛出的异常，也就是这个方法被调用时传递的第四个参数的值 */
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * 这个方法就是用来找到异常处理方法以及对他们进行排序处理的
     * <p>
     *     这个方法被设计成protected，是为了可以被继承，以便改写ExceptionHandlerExceptionResolver的异常处理逻辑，
     *     比如支持其它的注解或者有其它的排序算法等。给scanExHandleMethods与方法sortExHandleMethods设计为有参数的形式
     *     是为了增加灵活性，比如下面的重写方法就改变成了方法上有ExHandler注解的才是异常处理方法
     *     <pre class="code">
     *         &#064;Override
     *         protected void fetchExHandlMethods(){
     *             scanExHandleMethods(method->method.isAnnotationPresent(ExHandler.class));
     *             sortExHandleMethods(this.exHandleMethods);
     *         }
     *      </pre>
     *      而如果重写了scanExHandleMethods方法，那么可以完全改写扫描获取异常处理的逻辑，具有更大的灵活性
     * </p>
     */
    protected void fetchExHandlMethods(){
        scanExHandleMethods(method->method.isAnnotationPresent(ExceptionHandler.class));
        sortExHandleMethods(this.exHandleMethods);
    }

    /**
     * 此方法解析有任何修饰了ExceptionHandler注解的方法所在类，这些方法被认为是进行异常处理用的
     */
    protected void scanExHandleMethods(Predicate<Method> predicate) {
        List<Class<?>> classList = MvcContext.getMvcContext().getAllScannedClasses();

        for (Class<?> clz : classList) {
            Method[] methods = clz.getDeclaredMethods();
            for (Method method : methods) {
                if (predicate.test(method)) {
                    HandlerMethod exHandleMethod = new HandlerMethod(method);
                    exHandleMethods.add(exHandleMethod);
                }
            }
        }
    }

    /**
     * 这个方法是用来对所有的异常处理方法进行排序用的
     * @param exHandleMethods
     */
    protected void sortExHandleMethods(List<HandlerMethod> exHandleMethods) {
        Collections.sort(exHandleMethods, (m1, m2) ->
                m1.getHandlerMethod().getDeclaredAnnotation(ExceptionHandler.class).value()
                        .isAssignableFrom(m2.getHandlerMethod().getDeclaredAnnotation(ExceptionHandler.class).value()) ? 1 : -1);
    }

}
