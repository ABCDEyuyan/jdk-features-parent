package com.nf.demo.web.controller;

import com.nf.demo.entity.UserEntity;
import com.nf.demo.service.UserService;
import com.nf.demo.service.impl.UserServiceImpl;
import com.nf.mvc.ViewResult;
import com.nf.mvc.mapping.RequestMapping;
import com.nf.mvc.support.ResponseVO;

import static com.nf.mvc.handler.HandlerHelper.json;

@RequestMapping("/user")
public class UserController {
    private UserService userService = new UserServiceImpl();

    @RequestMapping("/login")
    public ViewResult login(String username, String pwd) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPwd(pwd);
        boolean valid = userService.login(userEntity);
        if (valid) {
            return json(ResponseVO.success(username));
        }else{
            return json(ResponseVO.fail());
        }
    }

}
