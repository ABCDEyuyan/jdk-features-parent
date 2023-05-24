package com.nf.web.controller;

import com.nf.entity.UserEntity;
import com.nf.mvc.argument.RequestBody;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.view.JsonViewResult;
import com.nf.service.Impl.LoginServiceImpl;
import com.nf.service.LoginService;
import com.nf.utils.vo.ResponseVo;

/**
 * @ClassName LoginController
 * @Author ZL
 * @Date 2023/5/12 9:13
 * @Version 1.0
 * @Explain
 **/
@RequestMapping("/user")
public class LoginController {
    LoginService service=new LoginServiceImpl();
    @RequestMapping("/login")
    public JsonViewResult login(UserEntity user){
        Boolean login = service.login(user);
        if (login) {
            return new JsonViewResult(new ResponseVo(200, "ok", user.getName()));
        } else {
            return new JsonViewResult(new ResponseVo(201, "no", null));
        }
    }
}
