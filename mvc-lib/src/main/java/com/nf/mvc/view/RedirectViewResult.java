package com.nf.mvc.view;

import com.nf.mvc.ViewResult;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class RedirectViewResult extends ViewResult {

    private String url;
    private Map<String,Object> model;

    public RedirectViewResult(String url) {
        this(url, new HashMap<>());
    }

    public RedirectViewResult(String url, Map<String, Object> model) {
        this.url = url;
        this.model = model;
    }

    @Override
    public void render(HttpServletRequest req, HttpServletResponse resp) throws Exception {
        this.url += initModel();
        resp.sendRedirect(url);
    }

    private  String initModel(){
        //ur?a=10&b=20
        if(model.size()==0) return "";

        StringBuilder builder = new StringBuilder("?");
        for (Map.Entry<String, Object> entry : model.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }

        //这里写大于2的逻辑是前面的？加上最后多余的那个&
        if(builder.length()>=2){
            builder.deleteCharAt(builder.length()-1);
        }
        return builder.toString();
    }


}
