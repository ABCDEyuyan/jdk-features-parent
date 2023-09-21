package mvcdemo.interceptor;

import com.nf.mvc.HandlerInterceptor;
import com.nf.mvc.Intercepts;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Intercepts(excludePattern = {"/product/**"})
public class ThirdInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("third pre---");
        return  true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("third post---");
        
    }
}
