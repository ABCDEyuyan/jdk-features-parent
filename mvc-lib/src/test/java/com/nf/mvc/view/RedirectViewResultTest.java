package com.nf.mvc.view;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class RedirectViewResultTest {
    @Test
    public void testUrl(){
        Map<String, Object> map = new HashMap<>();

        map.put("a", "100");
//        map.put("b", "200");
//        map.put("c", "300");

        if(map.size()==0) return;

        StringBuilder builder = new StringBuilder("?");
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            builder.append(entry.getKey());
            builder.append("=");
            builder.append(entry.getValue());
            builder.append("&");
        }

        //这里写大于2的逻辑是前面的？加上最后多余的那个&
        if(builder.length()>=2){
            builder.deleteCharAt(builder.length()-1);
        }
        System.out.println(builder.toString());
    }
}
